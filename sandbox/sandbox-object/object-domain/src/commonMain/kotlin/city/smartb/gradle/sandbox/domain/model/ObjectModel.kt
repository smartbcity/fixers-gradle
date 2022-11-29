package city.smartb.gradle.sandbox.domain.model

typealias ObjectId = String
typealias ObjectName = String

interface ObjectModelDTO {
    val id: ObjectId
    val name: ObjectName
}

data class ObjectModel(
    override val id: ObjectId,
    override val name: ObjectName,
): ObjectModelDTO
