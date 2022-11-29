package city.smartb.gradle.sandbox.domain.api

import city.smartb.gradle.sandbox.domain.model.ObjectModel
import city.smartb.gradle.sandbox.domain.model.ObjectName
import java.util.UUID

class ObjectService {

    fun create(objectName: ObjectName): ObjectModel {
        return ObjectModel(
            id = UUID.randomUUID().toString(),
            name = objectName
        )
    }
}
