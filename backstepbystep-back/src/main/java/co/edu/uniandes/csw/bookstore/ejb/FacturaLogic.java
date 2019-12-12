/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.ejb;

import co.edu.uniandes.csw.bookstore.entities.ClienteEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.FacturaEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.ClientePersistence;
import co.edu.uniandes.csw.bookstore.persistence.BookPersistence;
import co.edu.uniandes.csw.bookstore.persistence.FacturaPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Carlos Salazar
 */
@Stateless
public class FacturaLogic {

    @Inject
    private FacturaPersistence fp;
    @Inject
    private ClientePersistence cp;
    @Inject
    private BookPersistence dp;

    /**
     * Crea una factura por ID
     *
     * @param factura La entidad factura a crear
     * @param clienteId id del cliente
     *
     * @return La entidad factura luego de crearla
     * @throws BusinessLogicException <br>
     * Si los books de la factura están vacíos. <br>
     * Si el numero de la factura es menor a 0 o no existe <br>
     * Si el total pago de la factura es menor a 0 o no existe <br>
     * Si los impuestos de la factura son mernos a 0 o no existen <br>
     * Si Ya existe una factura con el mismo número <br>
     */
    public FacturaEntity createFactura(Long clienteId, FacturaEntity factura) throws BusinessLogicException {
        if (factura.getBooks() == null) {
            throw new BusinessLogicException("Los books de la factura están vacíos");
        } else if (factura.getNumeroDeFactura() == null || factura.getNumeroDeFactura() < 1) {
            throw new BusinessLogicException("El numero de la factura es menor a 0 o no existe");
        } else if (factura.getTotalPago() == null || factura.getTotalPago() < 0.0) {
            throw new BusinessLogicException("El total pago de la factura es menor a 0 o no existe");
        } else if (factura.getImpuestos() == null || factura.getImpuestos() < 0.0) {
            throw new BusinessLogicException("Los impuestos de la factura son mernos a 0 o no existen");
        } else if (fp.findByCode(factura.getNumeroDeFactura()) != null) {
            throw new BusinessLogicException("Ya existe una factura con el mismo número");
        } else if (cp.find(clienteId) == null) {
            throw new BusinessLogicException("No existe el cliente asociado a la factura");
        }
        for (BookEntity book : factura.getBooks()) {
            if (dp.find(book.getId()) == null) {
                throw new BusinessLogicException("El book no existe");
            }
        }
        ClienteEntity cliente = cp.find(clienteId);
        factura.setCliente(cliente);
        factura = fp.create(factura);
        return factura;
    }

    /**
     * Devuelve todas las facturas que hay en la base de datos asociadas al
     * cliente.
     *
     * @param clienteId id del cliente
     * @return Lista de entidades de tipo factura.
     */
    public List<FacturaEntity> getFacturas(Long clienteId) {
        ClienteEntity clienteEntity = cp.find(clienteId);
        return clienteEntity.getFacturas();
    }

    /**
     * Busca una factura por ID
     *
     * @param facturaId El id de la factura a buscar
     * @param clientesId id del cliente
     * @return La factura encontrada, null si no la encuentra.
     * @throws BusinessLogicException
     */
    public FacturaEntity getFactura(Long clientesId, Long facturaId) throws BusinessLogicException {
        List<FacturaEntity> factura = cp.find(clientesId).getFacturas();
        FacturaEntity facturaEntity = fp.find(clientesId, facturaId);
        int index = factura.indexOf(facturaEntity);
        if (index >= 0) {
            return factura.get(index);
        }
        throw new BusinessLogicException("La factura no está asociada a el cliente");
    }

    /**
     * Actualizar una factura por ID
     *
     * @param clienteId El ID del cliente de la factura
     * @param facturaEntity La entidad de la factura con los cambios deseados
     * @return La entidad factura luego de actualizarla
     * @throws BusinessLogicException <br>
     * Si los books de la factura están vacíos. <br>
     * Si el numero de la factura es menor a 0 o no existe <br>
     * Si el total pago de la factura es menor a 0 o no existe <br>
     * Si los impuestos de la factura son mernos a 0 o no existen <br>
     * Si Ya existe una factura con el mismo número <br>
     */
    public FacturaEntity updateFactura(Long clienteId, FacturaEntity facturaEntity) throws BusinessLogicException {
        if (facturaEntity.getBooks() == null) {
            throw new BusinessLogicException("Los books de la factura están vacíos");
        } else if (facturaEntity.getNumeroDeFactura() == null || facturaEntity.getNumeroDeFactura() < 1) {
            throw new BusinessLogicException("El numero de la factura es menor a 0 o no existe");
        } else if (facturaEntity.getTotalPago() == null || facturaEntity.getTotalPago() < 0.0) {
            throw new BusinessLogicException("El total pago de la factura es menor a 0 o no existe");
        } else if (facturaEntity.getImpuestos() == null || facturaEntity.getImpuestos() < 0.0) {
            throw new BusinessLogicException("Los impuestos de la factura son mernos a 0 o no existen");
        } else if (fp.findByCode(facturaEntity.getNumeroDeFactura()) != null && !facturaEntity.getId().equals(fp.findByCode(facturaEntity.getNumeroDeFactura()).getId())) {
            throw new BusinessLogicException("Ya existe una factura con el mismo número");
        } else if (cp.find(clienteId) == null) {
            throw new BusinessLogicException("No existe el cliente asociado al comprobante de pago");
        }
        for (BookEntity book : facturaEntity.getBooks()) {
            if (dp.find(book.getId()) == null) {
                throw new BusinessLogicException("El book no existe");
            }
        }
        ClienteEntity clienteEntity = cp.find(clienteId);
        facturaEntity.setCliente(clienteEntity);
        return fp.update(facturaEntity);
    }

    /**
     * Eliminar una factura por ID
     *
     * @param facturaId El ID de la factura a eliminar
     * @param clienteId id del cliente propietario de la factura
     * @throws BusinessLogicException
     */
    public void deleteFactura(Long facturaId, long clienteId) throws BusinessLogicException {
        FacturaEntity entity = getFactura(clienteId, facturaId);
        if (entity == null) {
            throw new BusinessLogicException("La factura con id = " + facturaId + " no esta asociada al cliente con id = " + clienteId);
        }
        fp.delete(entity.getId());
    }
}
