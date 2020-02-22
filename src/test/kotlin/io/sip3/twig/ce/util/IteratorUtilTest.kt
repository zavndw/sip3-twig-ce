/*
 * Copyright 2018-2020 SIP3.IO, Inc.
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

import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

class IteratorUtilTest {

    companion object {

        val EMPTY_ITERATOR = Collections.emptyIterator<Int>()
        val COMPARATOR = Comparator.comparingInt<Int> { i -> i }
    }

    @Test
    fun `Merge 2 empty Iterators`() {
        assertTrue(EMPTY_ITERATOR.equalsContent(EMPTY_ITERATOR.merge(EMPTY_ITERATOR)))
        assertTrue(EMPTY_ITERATOR.equalsContent(IteratorUtil.merge(EMPTY_ITERATOR, EMPTY_ITERATOR)))
        assertTrue(EMPTY_ITERATOR.equalsContent(EMPTY_ITERATOR.merge(EMPTY_ITERATOR, COMPARATOR)))
    }

    @Test
    fun `Merge 1 empty and 1 non-empty Iterators`() {
        val list = listOf(1, 3, 5)

        assertTrue(list.iterator().equalsContent(list.iterator().merge(EMPTY_ITERATOR)))
        assertTrue(list.iterator().equalsContent(IteratorUtil.merge(list.iterator(), EMPTY_ITERATOR)))
        assertTrue(list.iterator().equalsContent(list.iterator().merge(EMPTY_ITERATOR, COMPARATOR)))
    }

    @Test
    fun `Merge 2 non-empty Iterators`() {
        val i1 = listOf(1, 3, 5)
        val i2 = listOf(2, 4)

        var expected = listOf(1, 3, 5, 2, 4)
        assertTrue(expected.iterator().equalsContent(i1.iterator().merge(i2.iterator())))
        assertTrue(expected.iterator().equalsContent(IteratorUtil.merge(i1.iterator(), i2.iterator())))
        expected = listOf(1, 2, 3, 4, 5)
        assertTrue(expected.iterator().equalsContent(i1.iterator().merge(i2.iterator(), COMPARATOR)))
    }

    @Test
    fun `Map Iterator(Int) to Iterator(String)`() {
        val i1 = listOf(1, 2, 3)

        val expected = listOf("1", "2", "3")
        assertTrue(expected.iterator().equalsContent(i1.iterator().map(Any::toString)))
    }
}