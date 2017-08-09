package ncm.service.impl;

import ncm.dao.NCMCommentRepository;
import ncm.model.NCMCommentModel;
import ncm.service.NCMCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wenxiangzhou214164 on 2017/8/9.
 */
@RestController
public class NCMCommentServiceImpl implements NCMCommentService {

    private NCMCommentRepository ncmRepository;

    @Autowired
    public NCMCommentServiceImpl(NCMCommentRepository ncmCommentRepository) {
        this.ncmRepository = ncmCommentRepository;
    }

    @Override
    public void insert(NCMCommentModel ncmModel) {
        ncmRepository.save(ncmModel);
    }

    @Override
    public void insert(List<NCMCommentModel> ncmModelList) {
        ncmRepository.save(ncmModelList);
    }
}
