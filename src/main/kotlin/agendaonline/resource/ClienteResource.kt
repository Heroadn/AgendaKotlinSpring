package agendaonline.resource

import agendaonline.config.Property.AgendaOnlineProperty
import agendaonline.event.RecursoCriadoEvento
import agendaonline.exceptionHandler.exception.FileStorageException
import agendaonline.model.Cliente
import agendaonline.model.Token_
import agendaonline.model.Usuario
import agendaonline.repository.ClienteRepository
import agendaonline.repository.filter.ClienteFilter
import agendaonline.service.ClienteService
import org.codehaus.jackson.map.ObjectMapper
import org.springframework.boot.json.JsonParser
import org.springframework.boot.json.JsonParserFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.jwt.Jwt
import org.springframework.security.jwt.JwtHelper
import org.springframework.security.jwt.crypto.sign.MacSigner
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("/cliente")

/**
 * Classe responsavel por gerenciar o recurso 'Cliente'
 * @author  Benjamin de Castro Azevedo Ponciano
 * @version 1.0
 */
class ClienteResource(
        var clienteRepository: ClienteRepository,
        var publisher: ApplicationEventPublisher,
        var clienteService: ClienteService
){
    /**
     * Esse metodo é usado para buscar todos os clientes
     * @param clienteFilter Parametro usado para criar os criterios de pesquisa
     * @param pageable  Fornece informações da quantidade de paginas e registros,
     * deve ser usado o sufixo ?size=0&page=0, size = registros por pagina, page = pagina atual.
     */
    @GetMapping
    fun buscarTodos (clienteFilter: ClienteFilter,@PageableDefault(page = 0, size = 30) pageable: Pageable) : Page<Cliente> {
        return clienteRepository.filtrar(clienteFilter,pageable)
    }

    /**
     * Esse metodo é usado para buscar somente um cliente
     * @param id identificador do cliente desejado
     * @return retorna o cliente desejado(Code:200 OK),ou (Code: 404 Not Found)
     */
    @GetMapping("/{id}")
    fun buscarPorId (@PathVariable id:Long) : ResponseEntity<Cliente> {
        val cliente: Cliente? = clienteRepository.findOne(id)
        return if(cliente !== null) ResponseEntity.ok(cliente) else ResponseEntity.notFound().build()
    }

    /**
     * Esse metodo é usado para salvar no banco de dados o cliente,
     * ele usa os headers para acessar o Authorization que contem
     * o id do usuario,limitando o usuario a criar clientes so para
     * ele, caso a chave do token seja invalido a exception é lançada
     * @param headers recebe os cabeçalhos da requisição
     * @param cliente informações do cliente a serem gravadas
     * @param response resposta do servlet
     * @return retorna o usuario criado(201 Created)
     * @exception InvalidSignatureException,
     * @exception EmptyResultDataAccessException
     */
    @PostMapping
    fun criarCliente(@RequestHeader headers: HttpHeaders,@Valid @RequestBody cliente: Cliente,
                     response: HttpServletResponse) :  ResponseEntity<Cliente>
    {
        var clienteSalvo: Cliente = clienteService.salvarCliente(cliente,headers)
        publisher.publishEvent(RecursoCriadoEvento(this, response, clienteSalvo.id))
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo)
    }

    @PostMapping("/upload/{id}")
    fun uploadFile(@RequestParam file: MultipartFile, @PathVariable id:Long, request: HttpServletRequest, response: HttpServletResponse) : ResponseEntity<Any?> {
        var folderName:String = clienteService.buscarClientePorId(id).email
        var cliente: Cliente? = clienteService.salvarFoto(file, folderName, id)
        return ResponseEntity.status(HttpStatus.OK).body(cliente)
    }

    /**
     * Esse metodo responsavel por deletar o cliente, tem efeito em cascata,
     * deletando toda informação que tem relação com cliente
     * @param id identificado do cliente
     * @return retorna o cliente criado
     * @exception EmptyResultDataAccessException
     */
    @DeleteMapping("/{id_cliente}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarCliente(@PathVariable id_cliente:Long,@RequestHeader headers: HttpHeaders){
        var cliente = clienteService.deletarClienteDoUsuario(id_cliente,clienteService.getUsuarioByToken(headers))
        if(cliente != null) {
            clienteService.uploadService.deleteFiles(cliente.email)
            clienteService.uploadService.deleteDirectory(cliente.email)
        }
    }

    /**
     * Esse metodo é responsavel por atualizar o cliente
     * @param id identificado do cliente
     * @return retorna o cliente atualizado
     * @exception EmptyResultDataAccessException
     */
    //TODO:adicionar exemplo de retorno
    @PutMapping("/{id_cliente}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun atualizarUsuario(@PathVariable id_cliente:Long,@RequestBody clienteAtualizado: Cliente) : Cliente {
        var clienteBanco: Cliente = clienteService.clienteAtualizar(clienteAtualizado,id_cliente)
        return clienteBanco
    }
}