package model

abstract class Product {
  def id: Long
  def name: String
  def manufacturer: String
  def price: Double
  def description: String
  def productURl: String
  def quantity: Int
}
