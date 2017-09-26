import { OnInit, OnDestroy } from '@angular/core';
import { SelectComponent } from './select.component';
export declare class SelectOptionComponent implements OnInit, OnDestroy {
    protected select: SelectComponent;
    value: any;
    text: string;
    selected: boolean;
    hidden: boolean;
    constructor(select: SelectComponent);
    ngOnInit(): void;
    ngOnDestroy(): void;
    onClick(event: MouseEvent): void;
}
