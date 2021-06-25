package LogAnalyser.service;

import LogAnalyser.model.Logs;
import LogAnalyser.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    // To store the log data.
    public void addData(Logs logs) {
        logRepository.addLogs(logs);
    }

    // To get the count of uri stored in db.
    public int getUriCount() {
        int uris = logRepository.getURICount();
        if (uris != 0) {
            return uris;
        } else {
            return 0;
        }
    }

}
