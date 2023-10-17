package com.unicorn.soilmonitoring.app

data class OrcResult(
    val direction: Int,
    val log_id: Long,
    val words_result: List<WordsResult>,
    val words_result_num: Int
)

data class WordsResult(
    val words: String
)