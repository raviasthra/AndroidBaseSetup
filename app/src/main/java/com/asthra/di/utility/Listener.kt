package com.asthra.di.utility

typealias OnSuccess<R> = (R) -> Unit
typealias OnError<R> = (R) -> Unit

typealias OnDialogItemClickListener<R> = (R) -> Unit