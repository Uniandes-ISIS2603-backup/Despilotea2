/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.ejb;

import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.FacturaEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.FacturaPersistence;
import co.edu.uniandes.csw.bookstore.persistence.BookPersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Carlos Salazar
 */
@Stateless
public class FacturaBookLogic {

    private static final Logger LOGGER = Logger.getLogger(FacturaBookLogic.class.getName());

    @Inject
    private BookPersistence bookPersistence;

    @Inject
    private FacturaPersistence facturaPersistence;

    /**
     * Agregar un book a el factura
     *
     * @param booksId El id book a guardar
     * @param facturasId El id de el factura en la cual se va a guardar el
     * book.
     * @return El book creado.
     */
    public BookEntity addBook(Long booksId, Long facturasId, Long clientesId) {
        LOGGER.log(Level.INFO, "Inicia proceso de agregarle un book a el factura con id = {0}", facturasId);
        FacturaEntity facturaEntity = facturaPersistence.find(clientesId, facturasId);
        BookEntity bookEntity = bookPersistence.find(booksId);
        facturaEntity.getBooks().add(bookEntity);
        LOGGER.log(Level.INFO, "Termina proceso de agregarle un book a el factura con id = {0}", facturasId);
        return bookEntity;
    }

    /**
     * Retorna todos los books asociadas a una factura
     *
     * @param facturasId El ID de el factura buscada
     * @return La lista de books de el factura
     */
    public List<BookEntity> getBooks(Long facturasId, Long clientesId) {
        LOGGER.log(Level.INFO, "Inicia proceso de consultar los books asociadas a el factura con id = {0}", facturasId);
        return facturaPersistence.find(clientesId, facturasId).getBooks();
    }

    /**
     * Retorna un book asociado a una factura
     *
     * @param facturasId El id de el factura a buscar.
     * @param booksId El id del book a buscar
     * @return El book encontrado dentro de el factura.
     * @throws BusinessLogicException Si el book no se encuentra en el
     * factura
     */
    public BookEntity getBook(Long facturasId, Long booksId, Long clientesId) throws BusinessLogicException {
        List<BookEntity> book = facturaPersistence.find(clientesId, facturasId).getBooks();
        BookEntity bookEntity = bookPersistence.find(facturasId, booksId,clientesId);
        int index = book.indexOf(bookEntity);
        if (index >= 0) {
            return book.get(index);
        }
        throw new BusinessLogicException("La book no está asociada a el factura");
    }
}
