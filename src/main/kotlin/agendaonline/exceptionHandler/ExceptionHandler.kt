package agendaonline.exceptionHandler

import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import agendaonline.exceptionHandler.ExceptionHandler.Erro
import agendaonline.exceptionHandler.exception.ClienteInexistente
import agendaonline.exceptionHandler.exception.UsuarioInexistenteOuInativo
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException
import java.util.Arrays.asList



@ControllerAdvice
class ExceptionHandler(
        var messageSource: MessageSource
) : ResponseEntityExceptionHandler() {

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        var mensagemUsuario:String = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
        var mensagemDesenvolvedor:String = if(ex.cause != null) ex.cause.toString() else ex.toString()
        var erros:MutableList<Erro> = Arrays.asList(Erro(mensagemUsuario, mensagemDesenvolvedor))
        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(InvalidSignatureException::class)
    fun handleInvalidSignatureException(ex: InvalidSignatureException, request: WebRequest): ResponseEntity<Any> {
        val mensagemUsuario = messageSource.getMessage("jwt.invalido", null, LocaleContextHolder.getLocale())
        val mensagemDesenvolvedor = ex.toString()
        val erros = Arrays.asList(Erro(mensagemUsuario, mensagemDesenvolvedor))

        return handleExceptionInternal(ex, erros, HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handleEmptyResultDataAcessException(ex: EmptyResultDataAccessException, request: WebRequest): ResponseEntity<Any> {
        val mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale())
        val mensagemDesenvolvedor = ex.toString()
        val erros = Arrays.asList(Erro(mensagemUsuario, mensagemDesenvolvedor))

        return handleExceptionInternal(ex, erros, HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(UsuarioInexistenteOuInativo::class)
    fun handlePessoaInexistenteOuInativaException(ex: UsuarioInexistenteOuInativo): ResponseEntity<Any> {
        val mensagemUsuario = messageSource.getMessage("usuario.inexistente-ou-inativa", null, LocaleContextHolder.getLocale())
        val mensagemDesenvolvedor = ex.toString()
        val erros = Arrays.asList(Erro(mensagemUsuario, mensagemDesenvolvedor))
        return ResponseEntity.badRequest().body(erros)
    }

    @ExceptionHandler(ClienteInexistente::class)
    fun handleClienteInexistente(ex: ClienteInexistente): ResponseEntity<Any> {
        val mensagemUsuario = messageSource.getMessage("cliente.inexistente", null, LocaleContextHolder.getLocale())
        val mensagemDesenvolvedor = ex.toString()
        val erros = Arrays.asList(Erro(mensagemUsuario, mensagemDesenvolvedor))
        return ResponseEntity.badRequest().body(erros)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException, request: WebRequest): ResponseEntity<Any> {
        val mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale())
        val mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex)
        val erros = Arrays.asList(Erro(mensagemUsuario, mensagemDesenvolvedor))

        return handleExceptionInternal(ex, erros, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException,
                                              headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {

        val erros = criarListaDeErros(ex.bindingResult)
        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request)
    }

    fun criarListaDeErros(bindingResult: BindingResult): List<Erro> {
        val erros = ArrayList<Erro>()

        for (fieldError in bindingResult.fieldErrors) {
            val mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())
            val mensagemDesenvolvedor = fieldError.toString()
            erros.add(Erro(mensagemUsuario, mensagemDesenvolvedor))
        }
        return erros
    }

     class Erro{
        var mensagemUsuario:String = ""
        var mensagemDesenvolvedor:String = ""

        constructor(mensagemUsuario:String,mensagemDesenvolvedor:String) {
            this.mensagemUsuario = mensagemUsuario;
            this.mensagemDesenvolvedor = mensagemDesenvolvedor;
        }
    }
}