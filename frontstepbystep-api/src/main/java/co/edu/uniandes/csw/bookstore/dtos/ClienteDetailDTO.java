/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.dtos;

import co.edu.uniandes.csw.bookstore.entities.ClienteEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.FacturaEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Carlos Salazar
 */
public class ClienteDetailDTO extends ClienteDTO implements Serializable {

    /**
     * @return the facturas
     */
    public List<FacturaDTO> getFacturas() {
        return facturas;
    }

    /**
     * @param facturas the facturas to set
     */
    public void setFacturas(List<FacturaDTO> facturas) {
        this.facturas = facturas;
    }

    // relación  1 o muchos books
    private List<BookDTO> listaDeDeseos;
    private List<BookDTO> carritoDeCompras;
    private List<FacturaDTO> facturas;
    private MedioDePagoDTO metodoDePago;

    public ClienteDetailDTO() {
        super();
    }

    /**
     * Constructor para transformar un Entity a un DTO
     *
     * @param clienteEntity La entidad de la cual se construye el DTO
     */
    public ClienteDetailDTO(ClienteEntity clienteEntity) {
        super(clienteEntity);
        if (clienteEntity.getListaDeDeseos() != null) {
            listaDeDeseos = new ArrayList<>();
            for (BookEntity entityBook : clienteEntity.getListaDeDeseos()) {
                listaDeDeseos.add(new BookDTO(entityBook));
            }
        }
        if (clienteEntity.getCarritoDeCompras() != null) {
            carritoDeCompras = new ArrayList<>();
            for (BookEntity entityBook : clienteEntity.getCarritoDeCompras()) {
                carritoDeCompras.add(new BookDTO(entityBook));
            }
        }
        if (clienteEntity.getFacturas() != null) {
            facturas = new ArrayList<>();
            for (FacturaEntity entityFactura : clienteEntity.getFacturas()) {
                facturas.add(new FacturaDTO(entityFactura));
            }
        }
    }

    /**
     * Transformar el DTO a una entidad
     *
     * @return La entidad que representa al cliente.
     */
    @Override
    public ClienteEntity toEntity() {
        ClienteEntity clienteEntity = super.toEntity();
        if (getListaDeDeseos() != null) {
            List<BookEntity> booksEntity = new ArrayList<>();
            for (BookDTO book : getListaDeDeseos()) {
                booksEntity.add(book.toEntity());
            }
            clienteEntity.setListaDeDeseos(booksEntity);
        }
        if (getCarritoDeCompras() != null) {
            List<BookEntity> booksEntity = new ArrayList<>();
            for (BookDTO book : getCarritoDeCompras()) {
                booksEntity.add(book.toEntity());
            }
            clienteEntity.setCarritoDeCompras(booksEntity);
        }
        if (getFacturas() != null) {
            List<FacturaEntity> facturaEntity = new ArrayList<>();
            for (FacturaDTO factura : getFacturas()) {
                facturaEntity.add(factura.toEntity());
            }
            clienteEntity.setFacturas(facturaEntity);
        }
        return clienteEntity;
    }

    /**
     * @return the listaDeDeseos
     */
    public List<BookDTO> getListaDeDeseos() {
        return listaDeDeseos;
    }

    /**
     * @param listaDeDeseos the listaDeDeseos to set
     */
    public void setListaDeDeseos(List<BookDTO> listaDeDeseos) {
        this.listaDeDeseos = listaDeDeseos;
    }

    /**
     * @return the carritoDeCompras
     */
    public List<BookDTO> getCarritoDeCompras() {
        return carritoDeCompras;
    }

    /**
     * @param carritoDeCompras the carritoDeCompras to set
     */
    public void setCarritoDeCompras(List<BookDTO> carritoDeCompras) {
        this.carritoDeCompras = carritoDeCompras;
    }

    /**
     * @return the metodoDePago
     */
    public MedioDePagoDTO getMetodoDePago() {
        return metodoDePago;
    }

    /**
     * @param metodoDePago the metodoDePago to set
     */
    public void setMetodoDePago(MedioDePagoDTO metodoDePago) {
        this.metodoDePago = metodoDePago;
    }
}
