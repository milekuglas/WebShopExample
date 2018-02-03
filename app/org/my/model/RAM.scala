package org.my.model

case class RAM(productId: Long,
               ram_type: String,
               maxFrequency: Double,
               capacity: Int,
               voltage: Double,
               latency: Int)
