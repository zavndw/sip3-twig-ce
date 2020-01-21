/*
 * Copyright 2018-2019 SIP3.IO, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sip3.twig.ce.util

fun <T> Iterator<T>.merge(o: Iterator<T>, comparator: Comparator<T>? = null): Iterator<T> {
    val i = this
    return object : Iterator<T> {

        var vi: T? = i.nextOrNull()
        var vo: T? = o.nextOrNull()

        override fun hasNext(): Boolean {
            return vi != null || vo != null
        }

        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            val v: T?
            if (vi != null && (vo == null || comparator == null || comparator.compare(vi, vo) <= 0)) {
                v = vi
                vi = i.nextOrNull()
            } else {
                v = vo
                vo = o.nextOrNull()
            }
            return v!!
        }
    }
}

fun <T> Iterator<T>.nextOrNull(): T? {
    return if (hasNext()) next() else null
}

fun <T> Iterator<T>.equalsContent(o: Iterator<T>): Boolean {
    while (hasNext() && o.hasNext() && next() == o.next()) {
        // Do nothing...
    }
    return !hasNext() && !o.hasNext()
}