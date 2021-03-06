/*
 * Copyright 2020 Abdul-Mujeeb Aliu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mnsons.offlinebank.utils.ext


fun <T> List<T>.replaceItemInList(comparator: (item: T) -> Boolean, item: T): List<T> {
    val list = this.toMutableList()
    list.forEachIndexed { index, each ->
        each.takeIf { comparator(each) }?.let {
            list[index] = item
        }
    }
    return list
}
