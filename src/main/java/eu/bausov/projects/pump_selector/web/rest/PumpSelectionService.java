package eu.bausov.projects.pump_selector.web.rest;

import eu.bausov.projects.pump_selector.bo.Parameters;
import eu.bausov.projects.pump_selector.bo.equipment.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/PumpSelectionService")
@PreAuthorize("hasRole('ADMIN')")
public class PumpSelectionService {

    @Autowired
    private SessionFactory sessionFactory;

    @ResponseBody
    @RequestMapping(value = "/pumps", method = RequestMethod.POST)
    public List<PumpAggregate> getSuitablePumps(@RequestBody Parameters parameters) {

        Session currentSession = sessionFactory.getCurrentSession();

        // Get all equipment lists from DB
        List<Pump> pumps = currentSession.createCriteria(Pump.class).list();
        List<Reducer> reducers = currentSession.createCriteria(Reducer.class).list();
        List<Motor> motors = currentSession.createCriteria(Motor.class).list();
        List<Coupling> couplings = currentSession.createCriteria(Coupling.class).list();

//        private Constant constPumpType; +
//        private Double capacity;
//        private Integer pressure; +
//        private Integer viscosity;
//        private Double sg;
//        private Integer temperature; +
//        private Constant constCastingMaterial; +
//        private Seal seal; +
//        private Boolean isReliefValve; +
//        private Boolean isHeatingJacketed; +
//        private Boolean isExplosionProofed;

        List<PumpAggregate> pumpAggregates = new ArrayList<>();

        // Pump
        for (Pump pump : pumps) {
            if (pump.getConstPumpType().equals(parameters.getConstPumpType()) &&                    // pumpType
                    pump.getReliefValve() == parameters.getReliefValve() &&                         // reliefValve
                    (pump.getHeatingJacketOnCover() || pump.getHeatingJacketOnCasting() ||
                            pump.getHeatingJacketOnBracket()) == parameters.getHeatingJacket() &&   // heatingJacket
                    pump.getConstCastingMaterial().equals(parameters.getConstCastingMaterial()) &&  // castingMaterial
                    pump.getSeal().equals(parameters.getSeal()) &&                                  // seal
                    pump.isPressureValid(parameters) &&                                             // pressure
                    pump.isTemperatureValid(parameters)) {                                          // temperature
                // Reducer
                for (Reducer reducer : reducers) {
                    if (pump.isReducerValid(reducer, parameters)) {
                        // Motor
                        for (Motor motor : motors) {
                            if (motor.isMotorValid(reducer) &&
                                    parameters.getExplosionProof() == motor.isExplosionProofAvailable()) {
                                // Coupling
                                for (Coupling coupling : couplings) {
                                    if (pump.equals(coupling.getSuitablePump())) {
                                        PumpAggregate aggregate = new PumpAggregate();
                                        aggregate.setPump(pump);
                                        aggregate.setReducer(reducer);
                                        aggregate.setMotor(motor);
                                        aggregate.setCoupling(coupling);

                                        pumpAggregates.add(aggregate);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Temporary solution
        ///////////////////////////////////////////////////////////////////
        Pump pump = new Pump();
        pump.setModelName("Model name");
        PumpAggregate tmp = new PumpAggregate();
        tmp.setPump(pump);
        pumpAggregates.add(tmp);

        //return Arrays.asList("sd", "dd");

        return pumpAggregates;
    }
}

//@ResponseBody
    /*@RequestMapping(value = "/pumps", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public void getSuitablePumps(@RequestBody PumpAggregate aggregate) {
    }*/

//    @ResponseBody
//    @RequestMapping(value = "/pumps", method = RequestMethod.POST)
//    public String getSuitablePumps(@RequestBody Parameters params
//    ) {
//
//
//        return params.toString();
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/pumps", method = RequestMethod.GET)
//    @PreAuthorize("hasRole('ADMIN')")
//    //public List<String> getSuitablePumps(
//    public List<PumpAggregate> getSuitablePumps(
//            @RequestParam(required = false, value = "queryParam1") Integer p1,
//            @RequestParam(required = true, value = "queryParam2") String p2,
//            HttpSession httpSession
//    )