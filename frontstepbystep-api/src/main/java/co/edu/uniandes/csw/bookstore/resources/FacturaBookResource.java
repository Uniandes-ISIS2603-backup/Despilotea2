/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.resources;

import co.edu.uniandes.csw.bookstore.dtos.BookDTO;
import co.edu.uniandes.csw.bookstore.dtos.BookDetailDTO;
import co.edu.uniandes.csw.bookstore.ejb.FacturaBookLogic;
import co.edu.uniandes.csw.bookstore.ejb.BookLogic;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Estudiante
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FacturaBookResource {

    private static final Logger LOGGER = Logger.getLogger(FacturaBookResource.class.getName());

    @Inject
    private FacturaBookLogic facturaBookLogic; // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.

    @Inject
    private BookLogic bookLogic; // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.

    /**
     * Guarda un libro dentro de una factura con la informacion que recibe el la
     * URL. Se devuelve el libro que se guarda en la factura.
     *
     * @param facturaId Identificador de la factura que se esta actualizando.
     * Este debe ser una cadena de dígitos.
     * @param bookId Identificador del libro que se desea guardar. Este
     * debe ser una cadena de dígitos.
     * @param clienteId Identificador del cliente que se desea guardar. Este
     * debe ser una cadena de dígitos.
     * @return JSON {@link BookDTO} - El libro guardado en la factura.
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     * @throws BusinessLogicException
     */
    @POST
    @Path("{bookId: \\d+}")
    public BookDTO addBook(@PathParam("facturaId") Long facturaId, @PathParam("bookId") Long bookId, @PathParam("clienteId") Long clienteId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "FacturaBookResource addBook: input: facturasID: {0} , bookId: {1}", new Object[]{facturaId, bookId});
        if (bookLogic.getBook(bookId) == null) {
            throw new WebApplicationException("El recurso /books/" + bookId + " no existe.", 404);
        }
        BookDTO bookDTO = new BookDTO(facturaBookLogic.addBook(bookId, facturaId, clienteId));
        LOGGER.log(Level.INFO, "FacturaBookResource addBook: output: {0}", bookDTO);
        return bookDTO;
    }

    /**
     * Busca y devuelve todos los libros que existen en la factura
     * @param facturaId Identificador de la factura que se esta buscando. Este
     * @param clienteId Identificador del cliente que se desea guardar.
     * @return JSONArray {@link BookDetailDTO} - Los libros encontrados
     * en la factura. Si no hay ninguno retorna una lista vacía.
     */
    @GET
    public List<BookDetailDTO> getBooks(@PathParam("facturaId") Long facturaId, @PathParam("clienteId") Long clienteId) {
        LOGGER.log(Level.INFO, "FacturaBookResource getBook: input: {0}", facturaId);
        List<BookDetailDTO> listaDetailDTOs = booksListEntity2DTO(facturaBookLogic.getBooks(facturaId, clienteId));
        LOGGER.log(Level.INFO, "FacturaBookResource getBook: output: {0}", listaDetailDTOs);
        return listaDetailDTOs;
    }

    /**
     * Busca el libro con el id asociado dentro de la factura con id asociado.
     *
     * @param facturaId Identificador de la factura que se esta buscando. Este
     * debe ser una cadena de dígitos.
     * @param bookId Identificador del libro que se esta buscando. Este
     * @param clienteId Identificador del cliente que se desea guardar.
     * @return JSON {@link BookDetailDTO} - El libro buscado
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     * @throws BusinessLogicException {@link BusinessLogicExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro en la
     * factura.
     */
    @GET
    @Path("{bookId: \\d+}")
    public BookDetailDTO getBook(@PathParam("facturaId") Long facturaId, @PathParam("bookId") Long bookId, @PathParam("clienteId") Long clienteId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "FacturaBookResource getBook: input: facturasID: {0} , bookId: {1}", new Object[]{facturaId, bookId});
        if (bookLogic.getBook(facturaId, bookId, clienteId) == null) {
            throw new WebApplicationException("El recurso /facturas/" + facturaId + "/books/" + bookId + " no existe.", 404);
        }
        BookDetailDTO bookDetailDTO = new BookDetailDTO(facturaBookLogic.getBook(facturaId, bookId, clienteId));
        LOGGER.log(Level.INFO, "FacturaBookResource getBook: output: {0}", bookDetailDTO);
        return bookDetailDTO;
    }

    /**
     * Convierte una lista de BookEntity a una lista de
     * BookDetailDTO.
     *
     * @param entityList Lista de BookEntity a convertir.
     * @return Lista de BookDTO convertida.
     */
    private List<BookDetailDTO> booksListEntity2DTO(List<BookEntity> entityList) {
        List<BookDetailDTO> list = new ArrayList();
        for (BookEntity entity : entityList) {
            list.add(new BookDetailDTO(entity));
        }
        return list;
    }
}
