package ncm.service;

import ncm.model.NCMCommentModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by wenxiangzhou214164 on 2017/8/9.
 */
public interface NCMCommentService {

    @RequestMapping(value = "/comment/save", method = RequestMethod.GET)
    void insert(@RequestBody NCMCommentModel ncmModel);

    @RequestMapping(value = "/comment/saves", method = RequestMethod.GET)
    void insert(@RequestBody List<NCMCommentModel> ncmModelList);
}
