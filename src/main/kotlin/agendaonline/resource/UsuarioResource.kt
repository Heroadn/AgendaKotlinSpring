package agendaonline.resource

import agendaonline.event.RecursoCriadoEvento
import agendaonline.model.Usuario
import agendaonline.repository.UsuarioRepository
import agendaonline.repository.filter.UsuarioFilter
import agendaonline.service.UsuarioService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("/usuario")

/**
 * Classe responsavel por gerenciar o recurso 'Usuario'
 *
 * @author  Benjamin de Castro Azevedo Ponciano
 * @version 1.0
 */
class UsuarioResource(
        var usuarioRepository: UsuarioRepository,
        var publisher: ApplicationEventPublisher,
        var usuarioService: UsuarioService
){

    /**
     * Esse metodo é usado para buscar todos os usuarios
     * @param usuarioFilter Parametro usado para criar os criterios de pesquisa
     * @param pageable  Fornece informações da quantidade de paginas e registros,
     * deve ser usado o sufixo ?size=0&page=0, size = registros por pagina, page = pagina atual.
     */
    @GetMapping
    fun buscarTodos(usuarioFilter: UsuarioFilter,
                    @PageableDefault(page = 0, size = 30) pageable: Pageable) : Page<Usuario> {
        return usuarioRepository.filtrar(usuarioFilter,pageable)
    }

    /**
     * Esse metodo é usado para buscar somente um usuario
     * @param id identificador do usuario desejado
     * @return retorna o usuario desejado(Code:200 OK), ou (Code: 404 Not Found)
     */
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id:Long) : ResponseEntity<Usuario>{
        val usuario:Usuario? = usuarioRepository.findOne(id)
        return if(usuario !== null) ResponseEntity.ok(usuario) else ResponseEntity.notFound().build()
    }

    /**
     * Esse metodo é usado para salvar no banco de dados o usuario
     * @param usuario informações do usuario a serem gravadas
     * @param response resposta do servlet
     * @return retorna o usuario criado(201 Created)
     */
    @PostMapping
    fun salvarUsuario(@Valid @RequestBody usuario: Usuario, response: HttpServletResponse): ResponseEntity<Usuario> {
        var usuarioSalvo: Usuario = usuarioRepository.save(usuario)
        publisher.publishEvent(RecursoCriadoEvento(this, response, usuarioSalvo.id))
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo)
    }

    /**
     * Esse metodo responsavel por deletar o usuario, tem efeito em cascata,
     * deletando toda informação que tem relação com usuario
     * @param id identificado do usuario
     * @return retorna o usuario criado
     * @exception EmptyResultDataAccessException
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarUsuario(@PathVariable id:Long){
        var usuario = usuarioService.deletarUsuario(id)
        usuarioService.uploadService.deleteFiles(usuario.email)
        usuarioService.uploadService.deleteDirectory(usuario.email)
    }

    /**
     * Esse metodo é responsavel por atualizar o usuario
     * @param id identificado do usuario
     * @return retorna o usuario atualizado
     * @exception EmptyResultDataAccessException
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun atualizarUsuario(@PathVariable id:Long,@RequestBody usuario: Usuario) : Usuario{
        var usuarioBanco:Usuario = usuarioService.usuarioAtualizar(usuario,id)
        return usuarioBanco
    }

    /**
     * Responsavel por atualizar a propriedade ativo do usuario
     * @param id identificado do usuario
     * @param ativo propriedade a ser atualizada
     * @return retorna o usuario atualizado
     * @exception EmptyResultDataAccessException
     */
    //TODO:adicionar exemplo de retorno
    @PutMapping("/{id}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun atualizarPropriedades(@PathVariable id:Long,@RequestBody ativo:Boolean) : Usuario{
        var usuarioBanco:Usuario = usuarioService.usuarioAtualizarPropriedadeAtivo(ativo,id)
        return usuarioBanco
    }

    @PostMapping("/upload/{id}")
    fun uploadFile(@RequestParam file: MultipartFile, @PathVariable id:Long, request: HttpServletRequest, response: HttpServletResponse) : ResponseEntity<Any?> {
        var folderName:String = usuarioService.buscarUsuarioPorId(id).email
        var usuario:Usuario? = usuarioService.salvarFoto(file, folderName,id)
        return ResponseEntity.status(HttpStatus.OK).body(usuario)
    }
}