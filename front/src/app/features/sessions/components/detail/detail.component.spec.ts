import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { of } from 'rxjs/internal/observable/of';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let mockSessionApiService: any;
  let mockRouter: any;
  let mockMatSnackBar: any;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    mockSessionApiService = {
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({})),
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

    mockMatSnackBar = { open: jest.fn() };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
        MatButtonModule,
        BrowserAnimationsModule
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } },
        { provide: Router, useValue: mockRouter },

    ],
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
    component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');
  });

  it('should unparticipate from the session', () => {
    component.unParticipate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');
  });
  
  it('should fetch session details on init', () => {
    component.ngOnInit();
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
  });

  it('should delete the session', () => {
     mockSessionApiService.delete.mockReturnValue(of({}));
     jest.spyOn(mockRouter, 'navigate');

    component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
  });
  
  it('should navigate back', () => {
    const spy = jest.spyOn(window.history, 'back');
    component.back();
    expect(spy).toHaveBeenCalled();
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