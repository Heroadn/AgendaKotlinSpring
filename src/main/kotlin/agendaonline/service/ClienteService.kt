package agendaonline.service

import agendaonline.config.Property.AgendaOnlineProperty
import agendaonline.exceptionHandler.exception.ClienteInexistente
import agendaonline.exceptionHandler.exception.FileStorageException
import agendaonline.model.Cliente
import agendaonline.model.Usuario
import agendaonline.repository.AgendamentoRepository
import agendaonline.repository.ClienteRepository
import agendaonline.repository.UsuarioRepository
import org.springframework.beans.BeanUtils
import org.springframework.boot.json.JsonParserFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.security.jwt.JwtHelper
import org.springframework.security.jwt.crypto.sign.MacSigner

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path


@Service
class ClienteService(
    private var clienteRepository: ClienteRepository,
    private var usuarioRepository: UsuarioRepository,
    private var agendamentoRepository: AgendamentoRepository,
    var uploadService: UploadService
){

    fun buscarUsuarioPorId(id:Long) : Usuario {
        var usuario: Usuario? = usuarioRepository.findOne(id)
        if(usuario === null){
            throw EmptyResultDataAccessException(1)
        }

        return usuario
    }

    fun buscarClientePorId(id:Long) : Cliente {
        var cliente: Cliente? = clienteRepository.findOne(id)
        if(cliente === null){
            throw EmptyResultDataAccessException(1)
        }

        return cliente
    }

    fun salvarCliente(cliente: Cliente, headers: HttpHeaders): Cliente {
        var usuario = getUsuarioByToken(headers)

        //Salvando Cliente
        cliente.usuario = usuario
        return clienteRepository.save(cliente)
    }

    fun clienteAtualizar(cliente: Cliente, id: Long): Cliente {
        var clienteBanco:Cliente = buscarClientePorId(id)
        BeanUtils.copyProperties(cliente,clienteBanco,"id")
        return clienteRepository.save(clienteBanco)
    }

    /*Deleta o Cliente juntamente com seus agendamentos*/
    fun deletarClienteDoUsuario(id: Long, usuario: Usuario) :Cliente? {
        var successful = false

        //Limitando o Usuario a deletar somente seus usuarios
        var clienteBanco:Cliente? = null
        for(cliente in usuario.cliente) {
            if (cliente.id == id) {
                clienteBanco = buscarClientePorId(id)
                deletarAgendamentosDoCliente(cliente)
                clienteRepository.delete(cliente)
                successful = true
            }
        }

        if(successful == false) throw ClienteInexistente()
        return clienteBanco
    }

    fun clienteAtualizarPropriedadeFoto(foto: String, id: Long): Cliente {
        var clienteBanco:Cliente = buscarClientePorId(id)
        clienteBanco.foto= foto
        return clienteRepository.save(clienteBanco)
    }

    /*Deleta os agendamentos relacionados ao Cliente*/
    fun deletarAgendamentosDoCliente(cliente:Cliente){
        for(agendamento in agendamentoRepository.findAll()){
            if(agendamento.cliente.id == cliente.id){
                agendamentoRepository.delete(agendamento)
            }
        }
    }

    fun getUsuarioByToken(headers: HttpHeaders) : Usuario{
        /*Peguando JWT do Header Authorization, e pulando o prefixo 'Bearer'
        *ex: Bearer adadadada.adadad.adsadad*/
        var token:String = headers.get("Authorization")!![0].split(" ")[1]

        //Decodificando e passando para um mapa
        var map:Map<String,Any> = JsonParserFactory.getJsonParser().parseMap(
                JwtHelper.decodeAndVerify(token, MacSigner(AgendaOnlineProperty.getSeguranca().JTWSecret)).claims)

        //Peguando User_Id do JWT
        var usuario_id:Any? = map.get("user_id")
        return  buscarUsuarioPorId(usuario_id.toString().toLong())
    }

    fun salvarFoto(file: MultipartFile, folderName: String, id: Long): Cliente? {
        var path: Path? = uploadService.storeFile(file, folderName)
        var cliente: Cliente? = null

        if (path != null) {
            cliente = clienteAtualizarPropriedadeFoto(path.toString(), id)
        } else {
            FileStorageException("Path:null")
        }
        return cliente
    }

}