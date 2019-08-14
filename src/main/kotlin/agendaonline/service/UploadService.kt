package agendaonline.service

import agendaonline.exceptionHandler.exception.FileStorageException
import agendaonline.model.Cliente
import agendaonline.model.Usuario
import agendaonline.repository.ClienteRepository
import agendaonline.repository.UsuarioRepository
import org.omg.SendingContext.RunTime
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.lang.RuntimeException
import java.nio.file.StandardCopyOption
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Service
class UploadService(){

    val commonFolder:String = "./uploads/"//TODO:inserir na configuração
    val isPathAbsolute:Boolean = true

    fun storeFile(file: MultipartFile,folderName: String) : Path?{
        var fileStorageLocation:Path = getPathBasedOnUserName(commonFolder +folderName,isPathAbsolute)
        val fileName = file.originalFilename
        createDirectory(fileStorageLocation)

        if(validateImage(file)) {
            fileStorageLocation = saveImage(fileStorageLocation, file, fileName,folderName)
            println(fileStorageLocation)
        }

        return fileStorageLocation
    }

    fun deleteFiles(path:String){
        var folder:File = File(commonFolder+path)
        var allFiles:Array<File> = folder.listFiles()

        try {
            for(files in allFiles){
                files.delete()
            }
        } catch (ex: Exception) {
            throw FileStorageException("Erro ao Deletar diretorio:"+ex)
        }
    }

    fun deleteDirectory(folderName: String){
        try {
            Files.deleteIfExists(getPathBasedOnUserName(commonFolder+folderName,isPathAbsolute))
        } catch (ex: Exception) {
            throw FileStorageException("Erro ao Deletar diretorio:"+ex)
        }
    }

    private fun getRelativePathBasedOnUserName(folderName: String) : Path{
        return Paths.get("./${folderName}")
    }

    fun getPathBasedOnUserName(folderName: String,isAbsolute:Boolean) : Path{
        var path:Path? = null

        if(isAbsolute){
            path = Paths.get("./${folderName}")
                    .toAbsolutePath().normalize()
        }else{
            path = Paths.get("./${folderName}")
        }

        return path
    }

    private fun<T> saveImage(targetLocation: Path,file:MultipartFile,fileName:String,systemUser:T) : Path{
        var user:Any? = null
        var target:Path = targetLocation.resolve(fileName)

        try {
            Files.copy(file.inputStream, target, StandardCopyOption.REPLACE_EXISTING)
        } catch (ex: IOException) {
            throw FileStorageException("Não foi possivel armazenar Arquivo: $fileName, porfavor,tente noavamente!!")
        }

        return target
    }

    private fun createDirectory(path:Path){
        try {
            Files.createDirectories(path)
        } catch (ex: Exception) {
            throw FileStorageException("Erro ao criar diretorio")
        }
    }

    private fun validateImage(file: MultipartFile) : Boolean{
        val originalName = file.originalFilename
        var success:Boolean = true

        //TODO:Criar uma lista de validação para a imagem
        if (originalName.contains("..")) {
            success = false
            throw FileStorageException("Sorry! Filename contains invalid path sequence $originalName")
        }

        return success
    }
}
