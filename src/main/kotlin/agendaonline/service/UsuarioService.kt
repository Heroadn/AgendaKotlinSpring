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
import java.nio.file.Paths

@Service
class UsuarioService(
        private var clienteRepository: ClienteRepository,
        private var usuarioRepository: UsuarioRepository,
        private var agendamentoRepository: AgendamentoRepository,
        var uploadService: UploadService
) {
    /*Verifica se o usuario existe, caso nao exista uma exception é dispara*/
    fun buscarUsuarioPorId(id:Long) : Usuario {
        var usuario: Usuario? = usuarioRepository.findOne(id)
        if(usuario === null){
            throw EmptyResultDataAccessException(1)
        }
        return usuario
    }

    /*Verifica se o cliente existe, caso nao exista uma exception é dispara*/
    fun buscarClientePorId(id:Long) : Cliente {
        var cliente: Cliente? = clienteRepository.findOne(id)
        if(cliente === null){
            throw EmptyResultDataAccessException(1)
        }

        return cliente
    }

    /*Atualização completa do usuario*/
    fun usuarioAtualizar(usuario: Usuario, id: Long): Usuario {
        var usuarioBanco:Usuario = buscarUsuarioPorId(id)
        BeanUtils.copyProperties(usuario,usuarioBanco,"id")
        return usuarioRepository.save(usuarioBanco)
    }

    /*Atualização parcial do usuario somente propriedade ativo*/
    fun usuarioAtualizarPropriedadeAtivo(ativo: Boolean, id: Long): Usuario {
        var usuarioBanco:Usuario = buscarUsuarioPorId(id)
        usuarioBanco.ativo = ativo
        return usuarioRepository.save(usuarioBanco)
    }

    fun usuarioAtualizarPropriedadeFoto(foto: String, id: Long): Usuario {
        var usuarioBanco:Usuario = buscarUsuarioPorId(id)
        usuarioBanco.foto = foto
        return usuarioRepository.save(usuarioBanco)
    }

    /*Delete o Usuario juntamente com os clientes e agendamentos*/
    fun deletarUsuario(id: Long) : Usuario{
        var usuario = buscarUsuarioPorId(id)

        deletarClientesDoUsuario(usuario)
        deletarAgendamentosDoUsuario(usuario)
        usuarioRepository.delete(id)
        return usuario
    }

    /*Deleta o Cliente juntamente com seus agendamentos*/
    fun deletarClientesDoUsuario(usuario: Usuario) {
        //Limitando o Usuario a deletar somente seus usuarios
        for(cliente in usuario.cliente) {
                deletarAgendamentosDoCliente(cliente)
                deletarAgendamentosDoUsuario(usuario)
                clienteRepository.delete(cliente)
        }
    }

    /*Deleta os agendamentos relacionados ao Usuario*/
    fun deletarAgendamentosDoUsuario(usuario: Usuario){
        for(agendamento in agendamentoRepository.findAll()){
            if(agendamento.usuario.id == usuario.id){
                agendamentoRepository.delete(agendamento)
            }
        }
    }

    /*Deleta os agendamentos relacionados ao Cliente*/
    fun deletarAgendamentosDoCliente(cliente: Cliente){
        for(agendamento in agendamentoRepository.findAll()){
            if(agendamento.cliente.id == cliente.id){
                agendamentoRepository.delete(agendamento)
            }
        }
    }

    fun salvarFoto(file: MultipartFile, folderName: String, id: Long): Usuario? {
        var path: Path? = uploadService.storeFile(file, folderName)
        var usuario: Usuario? = null

        if (path != null) {
            usuario = usuarioAtualizarPropriedadeFoto(path.toString(), id)
        } else {
            throw FileStorageException("Path:null")
        }
        return usuario
    }

}
