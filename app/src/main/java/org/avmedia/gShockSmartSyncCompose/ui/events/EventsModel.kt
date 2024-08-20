/*
 * Created by Ivo Zivkov (izivkov@gmail.com) on 2022-03-30, 12:06 a.m.
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 2022-03-20, 7:47 p.m.
 */

package org.avmedia.gShockSmartSyncCompose.ui.events

import org.avmedia.gshockapi.Event

object EventsModel {

    private val events = ArrayList<Event>()

    fun isEmpty(): Boolean {
        return events.size == 0
    }

    fun getEvents(): ArrayList<Event> {
        return events
    }

    fun clear() {
        events.clear()
    }

    fun addAll(alarms: ArrayList<Event>) {
        this.events.addAll(alarms)
    }
}