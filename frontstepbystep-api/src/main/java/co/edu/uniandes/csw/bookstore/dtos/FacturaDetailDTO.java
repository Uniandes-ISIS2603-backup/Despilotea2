/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.dtos;

import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.FacturaEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Carlos Salazar
 */
public class FacturaDetailDTO extends FacturaDTO implements Serializable {

    // relación  1 o muchos books
    private List<BookDTO> books;

    public FacturaDetailDTO() {
        super();
    }

    /**
     * Constructor para transformar un Entity a un DTO
     *
     * @param facturaEntity La entidad de la cual se construye el DTO
     */
    public FacturaDetailDTO(FacturaEntity facturaEntity) {
        super(facturaEntity);
        if (facturaEntity.getBooks() != null) {
            books = new ArrayList<>();
            for (BookEntity entityBook : facturaEntity.getBooks()) {
                books.add(new BookDTO(entityBook));
            }
        }
    }

    /**
     * Transformar el DTO a una entidad
     *
     * @return La entidad que representa la factura.
     */
    @Override
    public FacturaEntity toEntity() {
        FacturaEntity facturaEntity = super.toEntity();
        if (books != null) {
            List<BookEntity> booksEntity = new ArrayList<>();
            for (BookDTO book : getBooks()) {
                booksEntity.add(book.toEntity());
            }
            facturaEntity.setBooks(booksEntity);
        }
        return facturaEntity;
    }

    /**
     * Devuelve los books de la factura
     *
     * @return DTO de books
     */
    public List<BookDTO> getBooks() {
        return books;
    }

    /**
     * Modifica los books de la factura
     *
     * @param booksE Lista de books
     */
    public void setBooks(List<BookDTO> booksE) {
        this.books = booksE;
    }

}
