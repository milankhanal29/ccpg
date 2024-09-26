import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductIdsDialogComponent } from './product-ids-dialog.component';

describe('ProductIdsDialogComponent', () => {
  let component: ProductIdsDialogComponent;
  let fixture: ComponentFixture<ProductIdsDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductIdsDialogComponent]
    });
    fixture = TestBed.createComponent(ProductIdsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
