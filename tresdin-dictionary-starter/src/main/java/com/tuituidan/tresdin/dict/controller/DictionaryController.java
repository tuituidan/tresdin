package com.tuituidan.tresdin.dict.controller;

import com.tuituidan.tresdin.dictionary.IDictionaryService;
import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import com.tuituidan.tresdin.dictionary.bean.DictType;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DictionaryController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/25
 */
@RestController
@RequestMapping("/api/v1")
public class DictionaryController {

    @Resource
    private IDictionaryService dictionaryService;

    /**
     * getDictInfoList
     *
     * @param type type
     * @return List
     */
    @GetMapping("/type/{type}/dict")
    public ResponseEntity<List<DictInfo>> getDictInfoList(@PathVariable("type") String type) {
        return ResponseEntity.ok(dictionaryService.getDictInfoList(type));
    }

    /**
     * getDictInfo
     *
     * @param type type
     * @param code code
     * @return IDictInfo
     */
    @GetMapping("/type/{type}/dict/{code}")
    public ResponseEntity<DictInfo> getDictInfo(@PathVariable("type") String type, @PathVariable("code") String code) {
        return ResponseEntity.ok(dictionaryService.getDictInfo(type, code));
    }

    /**
     * getDictType
     *
     * @param type type
     * @return IDictType
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<DictType> getDictInfo(@PathVariable("type") String type) {
        return ResponseEntity.ok(dictionaryService.getDictType(type));
    }

    /**
     * reloadCache
     *
     * @return Void
     */
    @GetMapping("/dict/actions/reload_cache")
    public ResponseEntity<Void> reloadCache() {
        dictionaryService.reloadCache();
        return ResponseEntity.noContent().build();
    }

}
