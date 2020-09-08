package com.thoughtworks.archgard.scanner2.infrastructure.persist

import com.thoughtworks.archgard.scanner2.domain.JMethodRepository
import com.thoughtworks.archgard.scanner2.domain.model.JField
import com.thoughtworks.archgard.scanner2.domain.model.JMethod
import com.thoughtworks.archgard.scanner2.infrastructure.TypeMap
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class JMethodRepositoryImpl(val jdbi: Jdbi) : JMethodRepository {
    private val log = LoggerFactory.getLogger(JClassRepositoryImpl::class.java)

    override fun findMethodFields(id: String): List<JField> {
        val sql = "SELECT id, name, type FROM JField WHERE id in (select b from _MethodFields where a='$id')"
        return jdbi.withHandle<List<JField>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JField::class.java))
            it.createQuery(sql)
                    .mapTo(JField::class.java)
                    .list()
        }
    }

    override fun findMethodsByModuleAndClass(systemId: Long, module: String, name: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access FROM JMethod WHERE clzname='$name' AND module='$module' AND system_id='$systemId'"
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodDto::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }


    override fun findMethodCallees(id: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access  FROM JMethod WHERE id IN (SELECT b FROM _MethodCallees WHERE a='$id') "
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodDto::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }
}

class JMethodDto(val id: String, val name: String, val clazz: String, val module: String, val returnType: String, val argumentTypes: String?, val access: String) {
    fun toJMethod(): JMethod {
        val argumentTypeList = if (argumentTypes.isNullOrBlank()) emptyList() else argumentTypes.split(",")
        val jMethod = JMethod(id, name, clazz, module, returnType, argumentTypeList)
        TypeMap.getMethodType(access.toInt()).forEach { jMethod.addType(it) }
        return jMethod
    }

}
