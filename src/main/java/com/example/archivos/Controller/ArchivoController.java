package com.example.archivos.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException; // Para mostrar las excepciones de los errores si hay
import java.nio.file.Files; // Para obtener informacion sobre archivos y directorios
import java.nio.file.Path;  // Se usa para trabajar con rutas de los directorios
import java.nio.file.Paths; // Se usa para trabajar con rutas de archivos y directorios importando primero la clase Path

@Controller 
@RequestMapping("/") //Se usa de modo normal el puerto 8080 como principal
public class ArchivoController {

    private static String UPLOADED_FOLDER = "D:\\subirArchivos\\"; //Se define la ruta en la que se va guardar el archivo subido

    @GetMapping
    public String index() {
        return "subirArchivo";  // Retorna  al formulario para la  subida de archivos
    }

    @PostMapping("/archivo") // se mapea con un nombre para el manejo de los archivos
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
      
        if (file.isEmpty()) //Si el archivo esta vacio mostrar ese mensaje
        {
            redirectAttributes.addFlashAttribute("mensaje", "Por favor selecciona un archivo para subir");
            return "redirect:/archivoStatus"; // retorna al html de archivoStatus
        }

        try {
            byte[] bytes = file.getBytes(); //el byte representa al archivo en un array de bytes 
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename()); //hace referencia a la ruta de la carpeta 
            //para guardar el archivo y el file.getOriginalFilename() nos da el nombre original del archivo que se subió. 
            // path será la ubicación completa donde guardaremos el archivo subido.
            Files.write(path, bytes); // escribe en el  byte del archivo

            redirectAttributes.addFlashAttribute("mensaje","Has subido exitosamente '" + file.getOriginalFilename() + "'"); // Muestra un mensaje para confirmar el exito de subida del archivo
            redirectAttributes.addFlashAttribute("progreso", 100); // Muestra el progreso de subida del archivo

        } catch (IOException e) // En esta parte captura si hay un error y muestra un mensaje 
         {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error durante la subida del archivo"); // Muestra que hubo un error al subir el archivo
            redirectAttributes.addFlashAttribute("progreso", 0); // Muestra el progreso de subida del archivo en cero porque hubo algun error
        }

        return "redirect:/archivoStatus";  // retorna al html de archivoStatus
    }

    @GetMapping("/archivoStatus") // Mapea a la vista de la subida de archivos
    public String cargarStatus() {
        return "archivoStatus"; // Vista del estado de la subida de archivos
    }
    
}
