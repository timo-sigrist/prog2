package ch.zhaw.pm2.arbeitsrapport.backend;

import ch.zhaw.pm2.arbeitsrapport.service.TextbausteinService;

public class TextbausteinServiceImplMock implements TextbausteinService {
    @Override
    public void test() {
        System.out.println("this is a mock of TextbausteinService");
    }
}
