package ch.zhaw.pm2.arbeitsrapport.backend;

import ch.zhaw.pm2.arbeitsrapport.service.ArbeitsrapportService;

public class ArbeitsrapportServiceImplMock implements ArbeitsrapportService {
    @Override
    public void test() {
        System.out.println("this is a mock of ArbeitsrapportService");
    }
}
