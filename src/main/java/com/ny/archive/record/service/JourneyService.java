package com.ny.archive.record.service;

import com.ny.archive.record.repository.JourneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JourneyService {

    private final JourneyRepository journeyRepository;
}
