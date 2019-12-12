/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.ejb;

import co.edu.uniandes.csw.bookstore.entities.ClienteEntity;
import co.edu.uniandes.csw.bookstore.entities.MedioDePagoEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.ClientePersistence;
import co.edu.uniandes.csw.bookstore.persistence.MedioDePagoPersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Juan Lozano
 */
@Stateless
public class MedioDePagoLogic {

    private static final Logger LOGGER = Logger.getLogger(MedioDePagoLogic.class.getName());

    @Inject
    private MedioDePagoPersistence persistence;
    @Inject
    private ClientePersistence cp;

    /* Guarda un nuevo medio de pago.
     *
     * @param medioDePagoEntity la entidad de tipo medio de pago que se desea
     * persistir.
     * @return La entidad que se desea persistir.
     * @throws BusinessLogicException Si el numero es inválido o ya existe en la
     * persistencia.
     */
    public MedioDePagoEntity createMedioDePago(Long clienteId, MedioDePagoEntity medioDePagoEntity) throws BusinessLogicException {

        LOGGER.log(Level.INFO, "Inicia proceso de creación de un medio de pago.");

        if (!verificarLasReglasNegocioMedioDePago(medioDePagoEntity)) {
            throw new BusinessLogicException("No pudo realizarse la creacion de un metodo de pago.");
        } else {
            ClienteEntity cliente = cp.find(clienteId);
            medioDePagoEntity.setCliente(cliente);
            medioDePagoEntity = persistence.create(medioDePagoEntity);
            persistence.create(medioDePagoEntity);
            LOGGER.log(Level.INFO, "Termina proceso de creación de un medio de pago.");
            return medioDePagoEntity;
        }

    }

    /**
     * Busca una medioDePago por ID
     *
     * @param medioDePagoId El id de la medioDePago a buscar
     * @param clientesId id del cliente
     * @return La medioDePago encontrada, null si no la encuentra.
     * @throws BusinessLogicException
     */
    public MedioDePagoEntity getMedioDePago(Long clientesId, Long medioDePagoId) throws BusinessLogicException {
        MedioDePagoEntity medioDePago = cp.find(clientesId).getMetodoDePago();
        if (medioDePago != null) {
            return medioDePago;
        }
        throw new BusinessLogicException("La medioDePago no está asociada a el cliente");
    }

    /**
     * Borrar un medio de pago.
     *
     * @param pMedio: id del medio de pago a borrar
     * @throws BusinessLogicException Si el medio de pago a eliminar.
     */
    public void deleteMedioDePago(Long pMedio) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de borrar el medio de pago con id = {0}", pMedio);
        persistence.delete(pMedio);
        LOGGER.log(Level.INFO, "Termina proceso de borrar el medio de pago con id = {0}", pMedio);
    }

    /**
     *
     * Actualizar un medio de pago.
     *
     * @param medioPago: medio de pago con los cambios para ser actualizada, por
     * ejemplo el nombre.
     * @return la medio de pago con los cambios actualizados en la base de
     * datos. Null en el caso de no poder actualizarla.
     */
    public MedioDePagoEntity updateMedioDePago(Long clienteId, MedioDePagoEntity medioPago) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de actualizar un medio de pago.");

        if (!verificarLasReglasNegocioMedioDePago(medioPago)) {
            throw new BusinessLogicException("No puede ser actualizada el medio de pago.");
        } else {
            ClienteEntity clienteEntity = cp.find(clienteId);
            medioPago.setCliente(clienteEntity);
            LOGGER.log(Level.INFO, "Termina proceso de actualizar un medio de pago.");
            return persistence.update(medioPago);

        }
    }

    /**
     * Verifica que el elemento enviado poLong clienteId,r parametro cumpla con
     * cada una de las reglas de negocio establecidas.
     *
     * @param medioDePagoEntity El medio de pago que se desea verificar que
     * cumpla con cada una de las reglas de negocio.
     * @throws
     * co.edu.uniandes.csw.dispositivos.exceptions.BusinessLogicException
     */
    public boolean verificarLasReglasNegocioMedioDePago(MedioDePagoEntity medioDePagoEntity) throws BusinessLogicException {

        if (medioDePagoEntity.getNumeroTarjeta().trim().equals("")) {
            throw new BusinessLogicException("No puede enviarse una cadena vacia.");

        }
        if (persistence.findByNumeroTarjeta(medioDePagoEntity.getNumeroTarjeta()) != null) {
            throw new BusinessLogicException("El medio de pago es inválida, ya que existe una con el numero de tarjeta." + medioDePagoEntity.getNumeroTarjeta());
        }

        if (medioDePagoEntity.getNumeroTarjeta().split("").length > 16) {
            throw new BusinessLogicException("La cantidad de numeros no corresponde con la esperada. Siendo " + medioDePagoEntity.getNumeroTarjeta().split("").length);
        }

        try {
            Double.parseDouble(medioDePagoEntity.getNumeroTarjeta());

        } catch (NumberFormatException e) {

            throw new BusinessLogicException("Los valores ingresados no corresponden a un valor numerico.");
        }

        if (medioDePagoEntity.getNumeroDeVerificacion().split("").length <= 0 && medioDePagoEntity.getNumeroDeVerificacion().split("").length > 3) {
            throw new BusinessLogicException("La cantidad de numeros no corresponde con la esperada: " + medioDePagoEntity.getNumeroTarjeta().split("").length);
        }

        try {
            Integer.parseInt(medioDePagoEntity.getNumeroDeVerificacion());

        } catch (Exception e) {
            throw new BusinessLogicException("La cantidad de numeros no corresponde con la esperada. Siendo " + medioDePagoEntity.getNumeroDeVerificacion().split("").length);
        }

        if (medioDePagoEntity.getTipoTarjeta().equalsIgnoreCase("VISA") && !medioDePagoEntity.getNumeroTarjeta().startsWith("4")) {
            char a = medioDePagoEntity.getNumeroTarjeta().charAt(0);
            throw new BusinessLogicException("Al ser una tarjeta del tipo Visa, debe iniciar con el numero 4 y este inicia con " + a);
        }

        if (medioDePagoEntity.getTipoTarjeta().equalsIgnoreCase("MASTERCARD") && !medioDePagoEntity.getNumeroTarjeta().startsWith("51") && !medioDePagoEntity.getNumeroTarjeta().startsWith("52") && !medioDePagoEntity.getNumeroTarjeta().startsWith("53") && !medioDePagoEntity.getNumeroTarjeta().startsWith("54") && !medioDePagoEntity.getNumeroTarjeta().startsWith("55")) {
            char a = medioDePagoEntity.getNumeroTarjeta().charAt(0);
            char b = medioDePagoEntity.getNumeroTarjeta().charAt(1);
            throw new BusinessLogicException("Al ser una tarjeta del tipo Mastercard, debe estar entre el rango de 51 a 55 y este inicia con " + a + b);
        }

        return true;
    }

    public void deleteMedioDePago(Long medioDePagoId, long clienteId) throws BusinessLogicException {
        MedioDePagoEntity entity = getMedioDePago(clienteId, medioDePagoId);
        if (entity == null) {
            throw new BusinessLogicException("La medioDePago con id = " + medioDePagoId + " no esta asociada al cliente con id = " + clienteId);
        }
        persistence.delete(entity.getId());
    }
}
