/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ResetuserComponent } from './resetuser.component';

describe('ResetuserComponent', () => {
  let component: ResetuserComponent;
  let fixture: ComponentFixture<ResetuserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResetuserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResetuserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
