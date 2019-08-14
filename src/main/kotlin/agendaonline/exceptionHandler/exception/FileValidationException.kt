package agendaonline.exceptionHandler.exception

class FileValidationException(var error:String) : RuntimeException(error) {
}