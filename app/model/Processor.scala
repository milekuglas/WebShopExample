package model

case class Processor (
  productId: Long,
  socket: String,
  processorType: String,
  cores: Int,
  cache: Int,
  thread: Int,
  baseFrequency: Double,
  turboFrequency: Double
)
