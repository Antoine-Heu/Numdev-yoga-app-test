import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  
  it('should return the initial isLogged value as false', (done) => {
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
      done();
    });
  });

  it('should update isLogged to true and emit the value when logIn is called', (done) => {
    const mockUser = { id: '1', admin: true } as any;
    service.logIn(mockUser);

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
      expect(service.sessionInformation).toEqual(mockUser);
      done();
    });
  });

  it('should update isLogged to false and emit the value when logOut is called', (done) => {
    service.logOut();

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
      expect(service.sessionInformation).toBeUndefined();
      done();
    });
  });

  it('should emit the correct value when next is called internally', (done) => {
    service['next']();

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
      done();
    });
  });
});
