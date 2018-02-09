package org.my.model

case class Category(
    id: Long,
    name: String,
    superCategoryId: Option[Long]
)
