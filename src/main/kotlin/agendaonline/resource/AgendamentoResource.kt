package agendaonline.resource

import agendaonline.event.RecursoCriadoEvento
import agendaonline.model.Agendamento
import agendaonline.model.StatusAgendamento
import agendaonline.repository.AgendamentoRepository
import agendaonline.repository.filter.AgendamentoFilter
import agendaonline.service.AgendamentoService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("agendamento")

/**
 * Classe responsavel por gerenciar o recurso 'Agendamento'
 *
 * @author  Benjamin de Castro Azevedo Ponciano
 * @version 1.0
 */
class AgendamentoResource(
        var agendamentoRepository: AgendamentoRepository,
        var publisher: ApplicationEventPublisher,
        var agendamentoService: AgendamentoService
) {
    /**
     * Esse metodo é usado para buscar todos os agendamentos
     * @param agendamentoFilter Parametro usado para criar os criterios de pesquisa
     * @param pageable  Fornece informações da quantidade de paginas e registros,
     * deve ser usado o sufixo ?size=0&page=0, size = registros por pagina, page = pagina atual.
     */
    @GetMapping
    fun buscarTodosAgendamentos(agendamentoFilter: AgendamentoFilter,
                                @PageableDefault(page = 0, size = 30) pageable: Pageable) : Page<Agendamento> {
        return agendamentoRepository.filtrar(agendamentoFilter,pageable);
    }

    /**
     * Esse metodo é usado para buscar somente um agendamento
     * @param id identificador do agendamento desejado
     * @return retorna o agendamento desejado(Code:200 OK),ou (Code: 404 Not Found)
     */
    @GetMapping("/{id}")
    fun buscarAgendamentoPorId(@PathVariable id:Long) : ResponseEntity<Agendamento>{
        var agendamento:Agendamento? = agendamentoService.buscarAgendamentoPorId(id)
        return if(agendamento !== null) ResponseEntity.ok(agendamento) else ResponseEntity.notFound().build()
    }

    /**
     * Esse metodo é usado para salvar no banco de dados o agendamento
     * @param usuario informações do agendamento a serem gravadas
     * @param response resposta do servlet
     * @return retorna o agendamento criado(201 Created)
     */
    @PostMapping
    fun salvarAgendamento(@Valid @RequestBody agendamento: Agendamento) : ResponseEntity<Agendamento>{
        var agendamentoBanco = agendamentoService.salvarAgendamento(agendamento)
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoBanco)
    }

    /**
     * Esse metodo responsavel por deletar o agendamento
     * @param id identificado do agendamento
     * @return retorna o agendamento criado
     * @exception EmptyResultDataAccessException
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarUsuario(@PathVariable id:Long){
        agendamentoRepository.delete(id)
    }

    /**
     * Esse metodo é responsavel por atualizar o agendamento
     * @param id identificado do agendamento
     * @return retorna o agendamento atualizado
     * @exception EmptyResultDataAccessException
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun atualizarUsuario(@PathVariable id:Long,@RequestBody agendamento: Agendamento,response: HttpServletResponse) : Agendamento {
        var agendamentoBanco: Agendamento = agendamentoService.agendamentoAtualizar(agendamento,id)
        publisher.publishEvent(RecursoCriadoEvento(this, response, agendamentoBanco.id))
        return agendamentoBanco
    }

    /**
     * Responsavel por atualizar a propriedade status do agendamento
     * @param id identificado do agendamento
     * @param status propriedade a ser atualizada
     * @return retorna o agendamento atualizado
     * @exception EmptyResultDataAccessException
     */
    //TODO:adicionar exemplo de retorno
    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun atualizarPropriedades(@PathVariable id:Long,@RequestBody status:StatusAgendamento) : Agendamento {
        var agendamentoBanco: Agendamento = agendamentoService.agendamentoAtualizarPropriedade(status,id)
        return agendamentoBanco
    }
}