package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.ModuleMember
import com.thoughtworks.archguard.module.infrastructure.dto.JClassDependencyDto
import com.thoughtworks.archguard.module.infrastructure.dto.JClassDto
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class JClassRepositoryImpl : JClassRepository {
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getJClassBy(name: String, module: String): JClass? {
        val sql = "select id, name, module, loc, access from JClass where name='$name' and module='$module'"
        return jdbi.withHandle<JClassDto, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .one()
        }.toJClass()
    }

    override fun getJClassById(id: String): JClass? {
        val sql = "select id, name, module, loc, access from JClass where id='$id'"
        return jdbi.withHandle<JClassDto, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .one()
        }.toJClass()
    }

    override fun getAll(): List<JClass> {
        val sql = "select id, name, module, loc, access from JClass"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun getJClassesHasModules(): List<JClass> {
        return this.getAll().filter { it.module != "null" }
    }

    override fun getAllClassDependency(members: List<ModuleMember>): List<Dependency<JClass>> {
        val tableTemplate = generateTableSqlTemplateWithModuleModules(members)

        val sql = "select a.module as moduleCaller, a.clzname as classCaller, " +
                "b.module as moduleCallee, b.clzname as classCallee " +
                "from ($tableTemplate) a, ($tableTemplate) b,  _MethodCallees mc " +
                "where a.id = mc.a and b.id = mc.b"
        return jdbi.withHandle<List<Dependency<JClass>>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDependencyDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDependencyDto::class.java)
                    .list()
                    .map { jClassDependencyDto -> jClassDependencyDto.toJClassDependency() }
                    .filter { dependency -> dependency.caller != dependency.callee }
        }
    }


}