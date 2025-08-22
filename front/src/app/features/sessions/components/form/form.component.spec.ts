import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockSessionApiService: any;
  let mockRouter: any;
  let mockActivatedRoute: any;
  let mockTeacherService: any;
  let mockMatSnackBar: any;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: '1'
    }
  };
  
  beforeEach(async () => {
    mockSessionApiService = {
       detail: jest.fn().mockReturnValue(of({
         name: 'Test Session',
         date: '2025-06-30',
         teacher_id: '1',
         description: 'Test Description',
       })),
       create: jest.fn().mockReturnValue(of({})),
       update: jest.fn().mockReturnValue(of({}))
     };
   
    mockRouter = {
      navigate: jest.fn(),
      url: '/sessions/update/1'
    };

    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1'), // Simule un paramètre d'URL "id"
        },
      },
    };

    mockTeacherService = {
      all: jest.fn().mockReturnValue(of([])),
    };

    mockMatSnackBar = {
      open: jest.fn(),
    };

    await TestBed.configureTestingModule({

      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: RouterTestingModule, useValue: mockRouter },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar }
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should initialize the form without fetching session details if URL does not include "update"', () => {
    mockRouter.url = '/sessions/create'; // Simule une URL sans "update"
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  
    expect(component.onUpdate).toBe(false);
    expect(component['id']).toBeUndefined();
    expect(mockSessionApiService.detail).not.toHaveBeenCalled();
  });

  it('should set onUpdate to true and fetch session details if URL includes "update"', () => {
    expect(component.onUpdate).toBe(true);
    expect(component['id']).toBe('1');
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form in update mode', () => {
    expect(component.onUpdate).toBe(true);
    expect(component.sessionForm?.value).toEqual({
      name: 'Test Session',
      date: '2023-01-01',
      teacher_id: '1',
      description: 'Test Description'
    });
  });

  it('should initialize form in create mode', () => {
    mockRouter.url = '/sessions/create';
    component.ngOnInit();
    expect(component.onUpdate).toBe(false);
    expect(component.sessionForm?.value).toEqual({
      name: '',
      date: '',
      teacher_id: '',
      description: ''
    });
  });

  it('should call create API on submit in create mode', () => {
    mockRouter.url = '/sessions/create';
    component.ngOnInit();
    component.sessionForm?.setValue({
      name: 'New Session',
      date: '2023-01-01',
      teacher_id: '2',
      description: 'New Description'
    });
    component.submit();
    expect(mockSessionApiService.create).toHaveBeenCalledWith({
      name: 'New Session',
      date: '2023-01-01',
      teacher_id: '2',
      description: 'New Description'
    });
  });

  it('should call update API on submit in update mode', () => {
    component.submit();
    expect(mockSessionApiService.update).toHaveBeenCalledWith('1', {
      name: 'Test Session',
      date: '2023-01-01',
      teacher_id: '1',
      description: 'Test Description'
    });
  });

  it('should navigate to sessions page after successful submission', () => {
    component.submit();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should redirect non-admin users to sessions page', () => {
    mockSessionService.sessionInformation.admin = false;
    component.ngOnInit();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });
});
