import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';

describe('SessionsService', () => {
  let service: SessionApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(SessionApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should show details of a session', (done) => {
    service.detail('1').subscribe((session) => {
      expect(session).toBeDefined();
      expect(session.name).toBeDefined();
      done();
    });
  });

  it('should get all sessions', (done) => {
    service.all().subscribe((sessions) => {
      expect(sessions).toBeDefined();
      expect(sessions.length).toBeGreaterThan(0);
      done();
    });
  });

  it('should delete a session', (done) => {
    service.delete('1').subscribe((response) => {
      expect(response).toBeDefined();
      done();
    });
  });

  it('should create a session', (done) => {
    const session = {
      name: 'New Session',
      date: new Date(),
      teacher_id: 1,
      description: 'Test Description',
      users: []
    };
    service.create(session).subscribe((createdSession) => {
      expect(createdSession).toBeDefined();
      expect(createdSession.name).toBe(session.name);
      done();
    });
  });

  it('should update a session', (done) => {
    const session = {
      name: 'Updated Session',
      date: new Date(),
      teacher_id: 1,
      description: 'Updated Description',
      users: []
    };
    service.update('1', session).subscribe((updatedSession) => {
      expect(updatedSession).toBeDefined();
      expect(updatedSession.name).toBe(session.name);
      done();
    });
  });

  it('should participate in a session', (done) => {
    service.participate('1', '1').subscribe((response) => {
      expect(response).toBeDefined();
      done();
    });
  });

  it('should un-participate from a session', (done) => {
    service.unParticipate('1', '1').subscribe((response) => {
      expect(response).toBeDefined();
      done();
    });
  });
  
});
