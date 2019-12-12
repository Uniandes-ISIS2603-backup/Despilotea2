/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.resources;

import co.edu.uniandes.csw.bookstore.dtos.MedioDePagoDTO;
import co.edu.uniandes.csw.bookstore.ejb.MedioDePagoLogic;
import co.edu.uniandes.csw.bookstore.entities.MedioDePagoEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Juan L
 */
@Path("medios")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class MedioDePagoResource {

    private static final Logger LOGGER = Logger.getLogger(MedioDePagoResource.class.getName());

    @Inject
    private MedioDePagoLogic medioLogic;

    @POST
    public MedioDePagoDTO createMedioDePago(@PathParam("clienteId") Long clienteId, MedioDePagoDTO medioDePago) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "MedioDePagoResource createMedioDePago: input: {0}", medioDePago);
        MedioDePagoDTO nuevoMedioDTO = new MedioDePagoDTO(medioLogic.createMedioDePago(clienteId, medioDePago.toEntity()));
        LOGGER.log(Level.INFO, "MedioDePagoResource createMedioDePago: output: {0}", nuevoMedioDTO);
        return nuevoMedioDTO;
    }

    private List<MedioDePagoDTO> listEntity2DetailDTO(List<MedioDePagoEntity> entityList) {
        List<MedioDePagoDTO> list = new ArrayList<>();
        for (MedioDePagoEntity entity : entityList) {
            list.add(new MedioDePagoDTO(entity));
        }
        return list;
    }

    @GET
    @Path("{medioDePagoId: \\d+}")
    public MedioDePagoDTO getMedioDePago(@PathParam("clienteId") Long clienteId, @PathParam("medioDePagoId") Long medioDePagoId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "MedioDePagoDePagoResource getMedioDePagos: input: {0}", medioDePagoId);
        MedioDePagoEntity medioDePagoEntity = medioLogic.getMedioDePago(clienteId, medioDePagoId);
        if (medioDePagoEntity == null) {
            throw new WebApplicationException("El recurso /clientes/" + clienteId + "/medioDePago/" + medioDePagoId + " no existe.", 404);
        }
        MedioDePagoDTO medioDePagoDetail = new MedioDePagoDTO(medioDePagoEntity);
        LOGGER.log(Level.INFO, "MedioDePagoDePagoResource getMedioDePagos: output: {0}", medioDePagoDetail);
        return medioDePagoDetail;
    }

    /**
     *
     * @param clienteId
     * @param medioDePagoId
     * @param medioDePago
     * @return
     * @throws BusinessLogicException
     */
    @PUT
    @Path("{medioDePagoId: \\d+}")
    public MedioDePagoDTO updateMedioDePago(@PathParam("clienteId") Long clienteId, @PathParam("medioDePagoId") Long medioDePagoId, MedioDePagoDTO medioDePago) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "MedioDePagoResource updateMedioDePago: input: clienteId: {0} , medioDePagoId: {1} , medioDePago:{2}", new Object[]{clienteId, medioDePagoId, medioDePago});
        if (!medioDePagoId.equals(medioDePago.getId())) {
            throw new BusinessLogicException("Los ids del medioDePago no coinciden.");
        }
        MedioDePagoEntity entity = medioLogic.getMedioDePago(clienteId, medioDePagoId);
        if (entity == null) {
            throw new WebApplicationException("El recurso /clientes/" + clienteId + "/medioDePago/" + medioDePagoId + " no existe.", 404);
        }
        MedioDePagoDTO medioDePagoDTO = new MedioDePagoDTO(medioLogic.updateMedioDePago(clienteId, medioDePago.toEntity()));
        LOGGER.log(Level.INFO, "MedioDePagoResource updateMedioDePago: output:{0}", medioDePagoDTO);
        return medioDePagoDTO;
    }

    /**
     *
     * @param clienteId
     * @param medioDePagoId
     * @throws BusinessLogicException
     */
    @DELETE
    @Path("{medioDePagoId: \\d+}")
    public void deleteMedioDePago(@PathParam("clienteId") Long clienteId, @PathParam("medioDePagoId") Long medioDePagoId) throws BusinessLogicException {
        MedioDePagoEntity entity = medioLogic.getMedioDePago(clienteId, medioDePagoId);
        if (entity == null) {
            throw new WebApplicationException("El recurso /clientes/" + clienteId + "/medioDePago/" + medioDePagoId + " no existe.", 404);
        }
        medioLogic.deleteMedioDePago(medioDePagoId, clienteId);
    }
}
