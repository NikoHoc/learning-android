package com.dicoding.dicodingstory

import com.dicoding.dicodingstory.data.local.entity.StoryEntity

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = StoryEntity(
                i.toString(),
                "author + $i",
                "story $i",
            )
            items.add(story)
        }
        return items
    }
}