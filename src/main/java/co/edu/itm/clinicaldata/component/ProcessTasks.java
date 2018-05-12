package co.edu.itm.clinicaldata.component;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import co.edu.itm.clinicaldata.queue.ProcessQueue;
import co.edu.itm.clinicaldata.service.ClusterService;

@Component
public class ProcessTasks {

    private static final String PROCESS_FINISHED = "Ha terminado el procesamiento de la solicitud con identificador <%s>";
    private static final String PROCESS_STARTED = "Comienza el procesamiento para la solicitud con identificador <%s>";

    private static final Logger LOGGER = Logger.getLogger(ProcessTasks.class.getName());

    @Autowired
    ClusterService clusterService;

    @Scheduled(fixedDelay = 10000)
    public void validateProcessQueue() {
        if(!ProcessQueue.getInstance().isEmpty()){
            String identifier = ProcessQueue.getInstance().get();
            LOGGER.info(String.format(PROCESS_STARTED, identifier));
            boolean hasEndProcess = clusterService.validateProcessState(identifier);
            if(hasEndProcess){
                ProcessQueue.getInstance().remove();
                LOGGER.info(String.format(PROCESS_FINISHED, identifier));
            }
        }
    }
}