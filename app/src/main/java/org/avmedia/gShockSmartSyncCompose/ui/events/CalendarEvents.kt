/*
 * Created by Ivo Zivkov (izivkov@gmail.com) on 2022-04-03, 6:13 p.m.
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 2022-04-03, 6:13 p.m.
 */

package org.avmedia.gShockSmartSyncCompose.ui.events

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.provider.CalendarContract
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.applicationContext
import org.avmedia.gshockapi.Event
import org.avmedia.gshockapi.EventDate
import org.avmedia.gshockapi.ProgressEvents
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

object CalendarEvents {
    fun getEventsFromCalendar(context: Context): ArrayList<Event> {
        return getEvents(context)
    }

    @SuppressLint("Range")
    private fun getEvents(context: Context): ArrayList<Event> {
        val events: ArrayList<Event> = ArrayList()
        val cur: Cursor?
        val cr: ContentResolver = context.contentResolver

        val mProjection = arrayOf(
            "_id",
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.HAS_ALARM,
            CalendarContract.Events.RRULE,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events._ID,
        )

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR, 0)

        val selection =
            """
            (${CalendarContract.Events.DTEND} >= ${calendar.timeInMillis}
            or ${CalendarContract.Events.RRULE} IS NOT NULL)
            and (${CalendarContract.Events.CALENDAR_ACCESS_LEVEL} = ?
            or ${CalendarContract.Events.HAS_ALARM} = "1")
            """.trimIndent()

        // Use this for non-google calendar
        // or ${CalendarContract.Events.TITLE} = "LOCAL"

        val selectionArgs = arrayOf(
            CalendarContract.Calendars.CAL_ACCESS_OWNER.toString()
        )

        val uri: Uri = CalendarContract.Events.CONTENT_URI
        cur = cr.query(
            uri,
            mProjection,
            selection,
            selectionArgs,
            CalendarContract.Events.DTSTART + " ASC" // could be null
        )

        CalendarObserver.register(cr, uri)

        while (cur!!.moveToNext()) {
            var title: String? = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE))
            title = if (title.isNullOrBlank()) "(No title)" else title

            val dateStart: String? =
                cur.getString(cur.getColumnIndex(CalendarContract.Events.DTSTART))
            val rrule: String? = cur.getString(cur.getColumnIndex(CalendarContract.Events.RRULE))
            val allDay: String? = cur.getString(cur.getColumnIndex(CalendarContract.Events.ALL_DAY))

            var zone = ZoneId.systemDefault()
            if (allDay == "1") {
                zone = ZoneId.of("UTC")
            }

            val startDate = EventsModel.createEventDate(dateStart!!.toLong(), zone)
            var endDate = startDate

            val (localEndDate, incompatible, daysOfWeek, repeatPeriod) =
                RRuleValues.getValues(rrule, startDate, zone)

            if (localEndDate != null) {
                endDate = EventDate(
                    localEndDate.year,
                    localEndDate.month,
                    localEndDate.dayOfMonth
                )
            }

            val end = LocalDate.of(endDate.year, endDate.month, endDate.day)
            if (!startDate.equals(endDate) && end.isBefore(LocalDate.now())) {
                continue // do not add expired events
            }

            val enabled = events.size < EventsModel.MAX_REMINDERS
            events.add(
                Event(
                    title,
                    startDate,
                    endDate,
                    repeatPeriod,
                    daysOfWeek,
                    enabled,
                    incompatible,
                )
            )
        }
        cur.close()
        return events
    }

    private object CalendarObserver {
        private var registered = false

        @Suppress("DEPRECATION")
        private val calendarObserver = object : ContentObserver(Handler()) {
            override fun onChange(selfChange: Boolean) {
                ProgressEvents.onNext("CalendarUpdated", getEvents(applicationContext()))
            }
        }

        fun register(cr: ContentResolver, uri: Uri) {
            if (!registered) {
                cr.registerContentObserver(uri, true, calendarObserver)
                registered = true
            }
        }
    }
}