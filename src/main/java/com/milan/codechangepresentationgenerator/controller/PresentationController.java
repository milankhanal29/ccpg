package com.milan.codechangepresentationgenerator.controller;

import com.milan.codechangepresentationgenerator.dto.PresentationRequest;
import com.milan.codechangepresentationgenerator.model.Presentation;
import com.milan.codechangepresentationgenerator.repository.PresentationRepository;
import com.milan.codechangepresentationgenerator.service.PresentationService;
import com.milan.codechangepresentationgenerator.util.DiffResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/presentations/")
public class PresentationController {
    @Autowired
    private PresentationService presentationService;

    @PostMapping("generate")
    public ResponseEntity<String> generatePresentation(@RequestBody PresentationRequest request) {
        try {
            // Extracting relevant data from the request
            String repoName = request.getRepoName();
            String commitId = request.getCommitId();
            String emailToUse = request.getEmailToUse();
            List<DiffResult> diffResults = request.getDiffResults(); // Assuming it's a list of DiffResult objects

            String presentationId = presentationService.createPresentation(diffResults, repoName, commitId,emailToUse);
            return ResponseEntity.ok(presentationId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating presentation: " + e.getMessage());
        }
    }
    @GetMapping("test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test");
    }

    @Autowired
    private PresentationRepository presentationRepository;

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadPresentation(@PathVariable("id") String id) {
        Presentation presentation = presentationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentation not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=presentation.pptx");

        return new ResponseEntity<>(presentation.getData(), headers, HttpStatus.OK);
    }
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<Presentation>> getPresentationsForUser(@PathVariable String userEmail) {
        List<Presentation> presentations = presentationService.getPresentationsForUserEmail(userEmail);
        return new ResponseEntity<>(presentations, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePresentation(@PathVariable String id) {
        presentationService.deletePresentation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    }