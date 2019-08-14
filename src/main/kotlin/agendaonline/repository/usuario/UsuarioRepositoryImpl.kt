package agendaonline.repository.usuario

import agendaonline.model.Usuario
import agendaonline.model.Usuario_
import agendaonline.repository.filter.UsuarioFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.util.StringUtils
import java.util.ArrayList
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class UsuarioRepositoryImpl(
        var manager: EntityManager
) : UsuarioRepositoryQuery {

    override fun filtrar(usuarioFilter: UsuarioFilter, pageable: Pageable): Page<Usuario> {
        var builder:CriteriaBuilder = manager.criteriaBuilder
        var criteria: CriteriaQuery<Usuario> = builder.createQuery(Usuario::class.java)
        var root:Root<Usuario> = criteria.from(Usuario::class.java)

        val predicates = criarRestricoes(usuarioFilter, builder, root)
        criteria.where(*predicates)

        val query = manager.createQuery(criteria)
        adicionarRestricoesDePaginacao(query, pageable)

        return PageImpl(query.resultList, pageable, total(usuarioFilter))
    }

    private fun total(usuarioFilter: UsuarioFilter): Long {
        val builder = manager.criteriaBuilder
        val criteria = builder.createQuery(Long::class.java)
        val root = criteria.from(Usuario::class.java)

        val predicates = criarRestricoes(usuarioFilter, builder, root)
        criteria.where(*predicates)

        criteria.select(builder.count(root))
        return manager.createQuery(criteria).singleResult
    }

    private fun adicionarRestricoesDePaginacao(query: TypedQuery<Usuario>, pageable: Pageable) {
        val paginaAtual = pageable.pageNumber
        val totalDeResguistrosPorPagina = pageable.pageSize
        val primeiroRegistroDa = paginaAtual * totalDeResguistrosPorPagina

        query.firstResult = primeiroRegistroDa
        query.maxResults = totalDeResguistrosPorPagina
    }

    private fun criarRestricoes(usuarioFilter: UsuarioFilter, builder: CriteriaBuilder, root: Root<Usuario>): Array<Predicate> {
        val predicates = ArrayList<Predicate>()

        if (!StringUtils.isEmpty(usuarioFilter.nome)) {
            predicates.add(
                    builder.equal(root.get(Usuario_.nome),usuarioFilter.nome))
        }

        if (!StringUtils.isEmpty(usuarioFilter.email)) {
            predicates.add(
                    builder.equal(root.get(Usuario_.email),usuarioFilter.email))
        }

        if (!StringUtils.isEmpty(usuarioFilter.celular)) {
            predicates.add(
                    builder.equal(root.get(Usuario_.celular),usuarioFilter.celular))
        }

        return predicates.toTypedArray()
    }
}