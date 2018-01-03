package model

case class Processor (
  id: Long,
  name: String,
  manufacturer: String,
  price: Double,
  description: String,
  productURl: String,
  quantity: Int,
  socket: String,
  processorType: String,
  cores: Int,
  cache: Int,
  thread: Int,
  baseFrequency: Double,
  turboFrequency: Double
)extends Product
