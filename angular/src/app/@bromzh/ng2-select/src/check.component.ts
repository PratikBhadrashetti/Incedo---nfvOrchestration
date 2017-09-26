import {
    Component,
    style,
    animate,
    transition,
    state,
    trigger, Input, ChangeDetectionStrategy,
} from '@angular/core';

@Component({
    selector: 'br-check',
    template: `
        <svg:svg viewBox="0 0 100 100" class="cp-check">
            <path class="cp-check-check" d="m 7.42,52.96 28.16,31.24 50.26,-68.07"
                [@checked]="checked ? 'on' : 'off'"/>
            <path class="cp-check-box" d="M 0,0 100,0 100,100 0,100z"/>
        </svg:svg>
    `,
    styles: [`
        :host {
            display: inline-block;
            width: 1em;
            height: 1em;
        }
        .cp-check-box {
            fill: none;
            stroke: #000000;
            stroke-width: 10;
        }
        .cp-check-check {
            stroke: #30cc10;
            stroke-width: 20px;
            stroke-linecap: square;
            stroke-linejoin: miter;
            fill: none;

            stroke-dasharray: 126.67, 126.67;
            stroke-dashoffset: 0;

            transform-origin: 50% 50%;
        }
    `],
    animations: [
        trigger('checked', [
            state('on', style({ strokeDashoffset: '*' })),
            state('off', style({ strokeDashoffset: '126.37', opacity: 0 })),
            transition('off => on', [
                style({ opacity: 1, transform: 'scale(1)' }),
                animate('250ms ease'),
            ]),
            transition('on => off', [
                animate('250ms ease', style({ transform: 'scale(2)', opacity: 0 })),
            ]),
        ]),
    ],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CheckComponent {
    @Input()
    checked: boolean;
}
