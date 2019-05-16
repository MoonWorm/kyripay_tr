package com.kyripay.traces.service;

import com.kyripay.traces.domain.trace.Trace;
import com.kyripay.traces.controller.SDRTracesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TracesService
{
  private final SDRTracesRepository repo;


  public Iterator<Trace> getAllTraces()
  {
    return repo.findAll().iterator();
  }


  public Optional<Trace> getTrace(long id)
  {
    return repo.findById(id);
  }


  public Trace addTrace(Trace trace)
  {
    trace.setCreated(LocalDateTime.now());
    return repo.save(trace);
  }


  public void update(Trace trace)
  {
    trace.setUpdated(LocalDateTime.now());
    repo.save(trace);
  }
}
