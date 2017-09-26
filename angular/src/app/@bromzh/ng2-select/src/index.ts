import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CheckComponent } from './check.component';
import { SelectComponent } from './select.component';
import { SelectOptionComponent } from './select-option.component';

export * from './check.component';
export * from './select.component';
export * from './select-option.component';

const SELECT_DIRECTIVES = [
    CheckComponent,
    SelectComponent,
    SelectOptionComponent,
];

@NgModule({
    imports: [ CommonModule, FormsModule ],
    declarations: [ SELECT_DIRECTIVES ],
    exports: [ SELECT_DIRECTIVES ],
})
export class SelectModule { }
