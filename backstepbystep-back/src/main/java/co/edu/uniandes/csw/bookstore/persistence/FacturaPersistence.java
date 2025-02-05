/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.persistence;

import co.edu.uniandes.csw.bookstore.entities.FacturaEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Carlos Salazar
 */
@Stateless
public class FacturaPersistence {

    @PersistenceContext(unitName = "BookStorePU")
    protected EntityManager em;

    public FacturaEntity create(FacturaEntity facturaEntity) {
        em.persist(facturaEntity);
        return facturaEntity;
    }

    /**
     * Devuelve todas las facturas de la base de datos.
     *
     * @return una lista con todas las facturas que encuentre en la base de
     * datos
     */
    public List<FacturaEntity> findAll() {
        TypedQuery query = em.createQuery("select u from FacturaEntity u", FacturaEntity.class);
        return query.getResultList();
    }

    /**
     * Busca si hay alguna factura con el id que se envía de argumento
     *
     * @param facturaId: id correspondiente a la factura buscada.
     * @return una factura.
     */
    /**
     * Busca un comprobante de pago por su id
     *
     * @param id llave del comprobante a buscar
     * @param clienteId id del cliente.
     * @return comprobante de pago correspondiente si lo encuentra, de lo
     * contrario null
     */
    public FacturaEntity find(Long clienteId, Long id) {
        TypedQuery<FacturaEntity> q = em.createQuery("select p from FacturaEntity p where (p.cliente.id = :clienteid) and (p.id = :facturaId)", FacturaEntity.class);
        q.setParameter("clienteid", clienteId);
        q.setParameter("facturaId", id);
        List<FacturaEntity> results = q.getResultList();
        FacturaEntity factura = null;
        if (results == null) {
            factura = null;
        } else if (results.isEmpty()) {
            factura = null;
        } else if (results.size() >= 1) {
            factura = results.get(0);
        }
        return factura;
    }

    /**
     * Busca si hay alguna Factura con el código que se envía de argumento
     *
     * @param code: código de la Factura que se está buscando
     * @return null si no existe ninguna factura con el código del parametro. Si
     * existe alguna devuelve la primera.
     */
    public FacturaEntity findByCode(Integer code) {
        TypedQuery query = em.createQuery("Select f From FacturaEntity f where f.numeroDeFactura = :code", FacturaEntity.class);
        query = query.setParameter("code", code);
        List<FacturaEntity> sameCode = query.getResultList();
        FacturaEntity result;
        if (sameCode == null) {
            result = null;
        } else if (sameCode.isEmpty()) {
            result = null;
        } else {
            result = sameCode.get(0);
        }
        return result;
    }

    /**
     * Actualiza una facturas.
     *
     * @param facturaEntity: la factura que viene con los nuevos cambios. Por
     * ejemplo el nombre pudo cambiar. En ese caso, se haria uso del método
     * update.
     * @return una factura con los cambios aplicados.
     */
    public FacturaEntity update(FacturaEntity facturaEntity) {
        return em.merge(facturaEntity);
    }

    /**
     * Borra una factura de la base de datos recibiendo como argumento el id de
     * la factura
     *
     * @param facturaId: id correspondiente a la factura a borrar.
     */
    public void delete(Long facturaId) {
        FacturaEntity facturaEntity = em.find(FacturaEntity.class, facturaId);
        em.remove(facturaEntity);
    }
}
