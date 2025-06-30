import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { of } from 'rxjs/internal/observable/of';
import { Router } from '@angular/router';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let mockSessionApiService: any;
  let mockRouter: any;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    mockSessionApiService = {
      participate: jest.fn().mockReturnValue(of({})),
      detail: jest.fn().mockReturnValue(of({
        id: '1',
        name: 'Test Session',
        date: new Date(),
        teacher_id: '1',
        description: 'Test Description',
        users: []
      })),
      delete: jest.fn().mockReturnValue(of({}))
    };

    mockRouter = {
      navigate: jest.fn(),
      url: '/sessions'
    }
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should participate in the session', () => {
    component.sessionId = '1';
    component.userId = '1';
    component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');
  });

  it('should unparticipate from the session', () => {
    component.sessionId = '1';
    component.userId = '1';
    component.unParticipate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');
  });
  
  it('should fetch session details on init', () => {
    component.sessionId = '1';
    component.ngOnInit();
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
  });

  it('should delete the session', () => {
    component.sessionId = '1';
    component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
  });

  it('should navigate to sessions after delete', () => {
    component.sessionId = '1';
    component.delete();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should navigate back', () => {
    const spy = jest.spyOn(window.history, 'back');
    component.back();
    expect(spy).toHaveBeenCalled();
  });
  
  it('should open a snackbar with a success message after deleting a session', () => {
    const snackBarSpy = jest.spyOn(component['matSnackBar'], 'open');
    component.sessionId = '1';
    component.delete();
    expect(snackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
  });
  
  it('should call delete method of sessionApiService with the correct sessionId', () => {
    component.sessionId = '1';
    component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
  });
  
  it('should navigate to sessions after successful deletion', () => {
    component.sessionId = '1';
    component.delete();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });
});

