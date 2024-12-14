package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepositorio;
    private AutorRepository autorRepositorio;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepositorio = libroRepository;
        this.autorRepositorio = autorRepository;
    }

    public void muestraElMenu() {
        var json = consumoApi.obtenerDatos(URL_BASE);
        
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    \n******** Menú ********
                    
                    1 - Buscar libro por título
                    2 - Lista de libros registrados
                    3 - Lista de autores registrados
                    4 - Lista de libros registrados por idioma
                    5 - Lista de autores vivos antes de un año determinado
                    
                    
                    0 - Salir
                    
                    Opción: 
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarLibrosPorIdioma();
                    break;
                case 5:
                    listarAutoresVivosPorAnio();
                    break;
                case 0:
                    System.out.println("\nCerrando la aplicación...\n");
                    break;
                default:
                    System.out.println("\nOpción inválida\n");
            }
        }

    }



    private Libro crearLibro(DatosLibros datosLibros, Autor autor) {
        if (autor != null) {
            return new Libro(datosLibros, autor);
        } else {
            System.out.println("El autor es null, no se puede crear el libro");
            return null;
        }
    }


    //Consumo desde la API de Gutendex
    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro:");
        var nombreLibro = teclado.nextLine();
        if (!nombreLibro.isBlank()){
            var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
            var datos = conversor.obtenerDatos(json, Datos.class);
            if (!datos.resultados().isEmpty()){
                DatosLibros datosLibros = datos.resultados().get(0);
                DatosAutor datosAutor = datosLibros.autor().get(0);
                Libro libro = null;
                Libro libroRepo = libroRepositorio.findByTitulo(datosLibros.titulo());
                if (libroRepo != null){
                    System.out.println("\nEste libro ya se encuentra registrado.");
//                    System.out.println(libroRepo.toString());
                } else {
                    Autor autorRepo = autorRepositorio.findByNombreIgnoreCase(datosLibros.autor().get(0).nombre());
                    if (autorRepo != null){
                        libro = crearLibro(datosLibros, autorRepo);
                        libroRepositorio.save(libro);
                        System.out.println("\nSe agregó un nuevo libro. \n");
                        System.out.println(libro);
                    } else {
                        Autor autor = new Autor(datosAutor);
                        autor = autorRepositorio.save(autor);
                        libro = crearLibro(datosLibros, autor);
                        libroRepositorio.save(libro);
                        System.out.println("\nSe agregó un nuevo libro. \n");
                        System.out.println(libro);
                    }
                }
            } else {
                System.out.println("\n El libro buscado no se pudo encontrar, ingresa otro");
            }
        } else {
            System.out.println("No ingresó un nombre valido.");
        }


    }

    //Consumo desde la DB
    private void listarLibrosRegistrados() {
        List<Libro> librosRegistrados = libroRepositorio.findAll();
        System.out.println("\n**************************");
        System.out.println("\nLos libros registrados en la base de datos son:");
        if (!librosRegistrados.isEmpty()){
            librosRegistrados.stream()
                    .forEach(System.out::println);
        } else {
            System.out.println("No hay ningún libro aún registrado en la base de datos.");
        }
    }

    private void listarAutoresRegistrados() {

        List<Autor> autoresRegistrados = autorRepositorio.findAll();
        System.out.println("\n**************************");
        System.out.println("\nLos autores registrados en la base de datos son: \n");
        if (!autoresRegistrados.isEmpty()){
            autoresRegistrados.stream()
                    .sorted(Comparator.comparing(Autor::getNombre))
                    .forEach(System.out::println);
        } else {
            System.out.println("No hay ningún autor aún registrado en la base de datos.");
        }
    }

    private void listarAutoresVivosPorAnio() {
        System.out.println("\nIngresa el año para listar los autores vivos:\n");
        var anio = teclado.nextInt();
        teclado.nextLine();
        if (anio > 0) {
            List<Autor> autorPorAnio = autorRepositorio
                    .findByAnioNacimientoLessThanEqualAndAnioMuerteGreaterThanEqual(anio, anio);
            if (!autorPorAnio.isEmpty()){
                System.out.println("\n**************************\n");
                System.out.println("Los autores vivos registrados en " + anio + " en la base de datos son: \n");
                autorPorAnio.stream()
                        .sorted(Comparator.comparing(Autor::getNombre))
                        .forEach(System.out::println);
            } else {
                System.out.println("No hay ningún autor aún registrado en " + anio + " en la base de datos.");
            }
        } else {
            System.out.println("Debe ingresar una fecha válida");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("\n**************************\n");
        System.out.println("Escribe el idioma de los libros que deseas consultar. Utilice solo las dos letras que estan entre []:\n");
        System.out.println("""
                [ES]: Español
                [EN]: Inglés
                [FR]: Francés
                [PT]: Portugués
                [IT]: Italiano
                """);
        var idiomaBuscado = teclado.nextLine().toLowerCase();
        if (!idiomaBuscado.isBlank()){
            if (idiomaBuscado.equals("es") ||
                    idiomaBuscado.equals("en") ||
                    idiomaBuscado.equals("fr") ||
                    idiomaBuscado.equals("pt") ||
                    idiomaBuscado.equals("it")
            ){
                List<Libro> librosBuscados = libroRepositorio.findByIdiomasContaining(idiomaBuscado);
                if (!librosBuscados.isEmpty()){
                    AtomicInteger contador = new AtomicInteger(0);
                    librosBuscados.stream()
                            .sorted(Comparator.comparing(Libro::toString))
                            .forEach(libro -> {
                                System.out.println(libro);
                                contador.incrementAndGet();
                            });
                    System.out.println("\n Estan registrados " + contador + " libros en " + "[" + idiomaBuscado + "]");
                } else {
                    System.out.println("No hay ningún libro con el idioma " + idiomaBuscado + " registrado en la base de datos");
                }
            } else {
                System.out.println("Ingresó un idioma inválido");
            }
        } else {
            System.out.println("No escribió ningún idioma.");
        }
    }
}

